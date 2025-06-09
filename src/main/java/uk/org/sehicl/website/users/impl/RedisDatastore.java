package uk.org.sehicl.website.users.impl;

import java.io.StringWriter;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import uk.org.sehicl.website.users.PasswordReset;
import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.UserDatastore;

public class RedisDatastore implements UserDatastore
{
    private final static ObjectMapper MAPPER = new JsonMapper()
            .configure(DeserializationFeature.USE_LONG_FOR_INTS, true);

    public static enum Bucket
    {
        USER_BY_EMAIL,
        USER_BY_ID,
        SESSION_BY_USER_ID,
        SESSION_BY_SESSION_ID,
        SESSION_ID_BY_EXPIRY,
        USER_ID_BY_SESSION_EXPIRY,
        RESET_BY_USER_ID,
        USER_ID_BY_RESET_EXPIRY
    }

    private static record ExpiryInfo(Bucket expiryBucket, long objKey, LongSupplier expiryGetter)
    {
        @SuppressWarnings("unchecked")
        void storeExpiryInDatabase(Jedis conn)
        {
            var bucketKey = expiryBucket.toString();
            var expKey = toStringKey(expiryGetter.getAsLong());
            var sKey = toStringKey(objKey);
            var keySet = new HashSet<>(List.of(sKey));
            var hget = conn.hget(bucketKey, expKey);
            Optional.ofNullable(hget).ifPresent(str ->
            {
                try
                {
                    var l = (List<String>) MAPPER.readValue(str, List.class);
                    keySet.addAll(l);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            });
            StringWriter sw = new StringWriter();
            try
            {
                MAPPER.writeValue(sw, keySet);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            conn.hset(bucketKey, expKey, sw.toString());
        }
    };

    private static String USER_ID_COUNTER = "USER_ID";

    private final JedisPool pool;

    public RedisDatastore(String uri, String auth)
    {
        pool = new JedisPool(URI.create(uri));
    }

    Jedis connect()
    {
        return pool.getResource();
    }

    static String toStringKey(long key)
    {
        return "%d".formatted(key);
    }

    Optional<String> getValue(Jedis conn, Bucket bucket, String key)
    {
        return Optional.ofNullable(conn.hget(bucket.toString(), key));
    }

    Optional<String> getValue(Jedis conn, Bucket bucket, long key)
    {
        return getValue(conn, bucket, toStringKey(key));
    }

    <T> Function<String, T> fromJson(Class<T> cl)
    {
        return str ->
        {
            try
            {
                return MAPPER.readValue(str, cl);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public User getUserByEmail(String email)
    {
        try (var conn = connect())
        {
            return getUserByEmail(conn, email);
        }
    }

    public User getUserByEmail(Jedis conn, String email)
    {
        return getValue(conn, Bucket.USER_BY_EMAIL, email).map(fromJson(User.class)).orElse(null);
    }

    @Override
    public User getUserById(long id)
    {
        try (var conn = connect())
        {
            return getUserById(conn, id);
        }
    }

    public User getUserById(Jedis conn, long id)
    {
        return getValue(conn, Bucket.USER_BY_ID, id).map(fromJson(User.class)).orElse(null);
    }

    @Override
    public SessionData getSessionByUserId(long id)
    {
        try (var conn = connect())
        {
            return getSessionByUserId(conn, id);
        }
    }

    SessionData getSessionByUserId(Jedis conn, long id)
    {
        return getValue(conn, Bucket.SESSION_BY_USER_ID, id)
                .map(fromJson(SessionData.class))
                .orElse(null);
    }

    @Override
    public SessionData getSessionBySessionId(long id)
    {
        try (var conn = connect())
        {
            return getSessionBySessionId(conn, id);
        }
    }

    SessionData getSessionBySessionId(Jedis conn, long id)
    {
        return getValue(conn, Bucket.SESSION_BY_SESSION_ID, id)
                .map(fromJson(SessionData.class))
                .orElse(null);
    }

    <T> void putValue(Jedis conn, Bucket bucket, String key, T obj)
    {
        putValue(conn, bucket, key, obj, null);
    }

    <T> void putValue(Jedis conn, Bucket bucket, long key, T obj)
    {
        putValue(conn, bucket, key, obj, null);
    }

    <T> void putValue(Jedis conn, Bucket bucket, String key, T obj, ExpiryInfo expiry)
    {
        var bucketName = bucket.toString();
        conn.hset(bucketName, key, toJson(obj));
        try
        {
            if (expiry != null)
                conn
                        .hexpireAt(bucketName,
                                TimeUnit.MILLISECONDS.toSeconds(expiry.expiryGetter.getAsLong()),
                                key);
        }
        catch (Exception ex)
        {
            expiry.storeExpiryInDatabase(conn);
        }
    }

    void deleteValue(Jedis conn, Bucket bucket, String key)
    {
        conn.hdel(bucket.toString(), key);
    }

    void deleteValue(Jedis conn, Bucket bucket, long key)
    {
        deleteValue(conn, bucket, toStringKey(key));
    }

    <T> void putValue(Jedis conn, Bucket bucket, long key, T obj, ExpiryInfo expiry)
    {
        putValue(conn, bucket, toStringKey(key), obj, expiry);
    }

    <T> String toJson(T obj)
    {
        try
        {
            var sw = new StringWriter();
            MAPPER.writeValue(sw, obj);
            sw.close();
            return sw.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SessionData setSession(User user)
    {
        var expiryInSeconds = TimeUnit.DAYS.toSeconds(1);
        return setSession(user, expiryInSeconds);
    }

    SessionData setSession(User user, long expiryInSeconds)
    {
        try (var conn = connect())
        {
            SessionData answer = getSessionByUserId(conn, user.getId());
            if (answer == null)
                answer = new SessionData(getSessionId(user), user.getId(),
                        new Date().getTime() + TimeUnit.SECONDS.toMillis(expiryInSeconds));
            putValue(conn, Bucket.SESSION_BY_SESSION_ID, answer.getId(), answer,
                    new ExpiryInfo(Bucket.SESSION_ID_BY_EXPIRY, answer.getId(), answer::getExpiry));
            putValue(conn, Bucket.SESSION_BY_USER_ID, answer.getUserId(), answer, new ExpiryInfo(
                    Bucket.USER_ID_BY_SESSION_EXPIRY, answer.getUserId(), answer::getExpiry));
            return answer;
        }
    }

    long getSessionId(User user)
    {
        long factor = 10000000000L;
        final long time = new Date().getTime();
        long answer = user.getId() * factor + time % factor;
        return answer;
    }

    @Override
    public void clearExpiredSessions()
    {
        try (var conn = connect())
        {
            clearExpiredSessions(conn);
        }
    }

    void clearExpiredSessions(Jedis conn)
    {
        clearExpiredEntries(conn, Bucket.SESSION_ID_BY_EXPIRY, Bucket.SESSION_BY_SESSION_ID);
        clearExpiredEntries(conn, Bucket.USER_ID_BY_SESSION_EXPIRY, Bucket.SESSION_BY_USER_ID);
    }

    @Override
    public User createUser(User user)
    {
        try (var conn = connect())
        {
            user.setId(conn.incr(USER_ID_COUNTER));
            putValue(conn, Bucket.USER_BY_ID, user.getId(), user);
            putValue(conn, Bucket.USER_BY_EMAIL, user.getEmail(), user);
            return user;
        }
    }

    @Override
    public void updateUser(User user)
    {
        try (var conn = connect())
        {
            putValue(conn, Bucket.USER_BY_ID, user.getId(), user);
            putValue(conn, Bucket.USER_BY_EMAIL, user.getEmail(), user);
        }
    }

    @Override
    public PasswordReset generatePasswordReset(String email)
    {
        return generatePasswordReset(email, TimeUnit.HOURS.toSeconds(3));
    }

    public PasswordReset generatePasswordReset(String email, long expiryInSeconds)
    {
        PasswordReset answer = null;
        try (var conn = connect())
        {
            var user = getUserByEmail(conn, email);
            if (user != null)
            {
                answer = new PasswordReset(user.getId(), email);
                answer.setExpiry(new Date().getTime() + TimeUnit.SECONDS.toMillis(expiryInSeconds));
                putValue(conn, Bucket.RESET_BY_USER_ID, user.getId(), answer, new ExpiryInfo(
                        Bucket.USER_ID_BY_RESET_EXPIRY, user.getId(), answer::getExpiry));
            }
            return answer;
        }
    }

    @Override
    public PasswordReset getPasswordReset(long id)
    {
        try (var conn = connect())
        {
            return getValue(conn, Bucket.RESET_BY_USER_ID, id)
                    .map(fromJson(PasswordReset.class))
                    .orElse(null);
        }
    }

    @Override
    public void clearExpiredResets()
    {
        try (var conn = connect())
        {
            clearExpiredResets(conn);
        }
    }

    void clearExpiredResets(Jedis conn)
    {
        clearExpiredEntries(conn, Bucket.USER_ID_BY_RESET_EXPIRY, Bucket.RESET_BY_USER_ID);
    }

    void clearExpiredEntries(Jedis conn, Bucket expiryBucket, Bucket objectBucket)
    {
        var now = new Date().getTime();
        var expiryBucketKey = expiryBucket.toString();
        var pastExpiries = conn
                .hkeys(expiryBucketKey)
                .stream()
                .filter(k -> Long.parseLong(k) < now)
                .toArray(String[]::new);
        @SuppressWarnings("unchecked")
        var expiredKeys = Stream
                .of(pastExpiries)
                .map(k -> conn.hget(expiryBucketKey, k))
                .flatMap(j ->
                {
                    try
                    {
                        return ((List<Long>) MAPPER.readValue(j, List.class)).stream();
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(String[]::new);
        Stream.of(pastExpiries).forEach(k -> conn.hdel(expiryBucketKey, k));
        Stream.of(expiredKeys).forEach(k -> conn.hdel(objectBucket.toString(), k));
    }

    @Override
    public Collection<Long> getAllUserIds()
    {
        try (var conn = connect())
        {
            return conn.hkeys(Bucket.USER_BY_ID.toString()).stream().map(Long::parseLong).toList();
        }
    }

    @Override
    public void deleteUser(long id)
    {
        try (var conn = connect())
        {
            User user = getUserById(conn, id);
            if (user == null)
                return;
            deleteValue(conn, Bucket.USER_BY_ID, id);
            deleteValue(conn, Bucket.USER_BY_EMAIL, user.getEmail());
            deleteValue(conn, Bucket.RESET_BY_USER_ID, id);
            var session = getSessionByUserId(conn, id);
            if (session == null)
                return;
            deleteValue(conn, Bucket.SESSION_BY_USER_ID, id);
            deleteValue(conn, Bucket.SESSION_BY_SESSION_ID, session.getId());
        }
    }

}
