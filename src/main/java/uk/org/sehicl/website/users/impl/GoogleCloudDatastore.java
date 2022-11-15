package uk.org.sehicl.website.users.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;

import uk.org.sehicl.website.users.PasswordReset;
import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.UserDatastore;

public class GoogleCloudDatastore implements UserDatastore
{
    private static enum Prefix
    {
        EMAIL,
        USERID,
        SESSIONID,
        SESSIONUSER,
        PWRESET;

        public String toString()
        {
            return String.format("%s-", name().toLowerCase());
        }

        public String key(String value)
        {
            return String.format("%s%s", this, value);
        }

        public String key(long value)
        {
            return String.format("%s%d", this, value);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCloudDatastore.class);

    public GoogleCloudDatastore()
    {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        storage.get("sehicl-users").list().iterateAll().forEach(b -> LOG.info(b.getName()));
    }

    private Bucket usersBucket()
    {
        return StorageOptions.getDefaultInstance().getService().get("sehicl-users");
    }

    private <T> String toYaml(T obj)
    {
        try
        {
            return new ObjectMapper(new YAMLFactory()).writeValueAsString(obj);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    private <T> Function<Blob, T> fromBlob(Class<T> type)
    {
        return blob ->
        {
            if (blob == null)
                return null;
            try
            {
                return new ObjectMapper(new YAMLFactory()).readValue(blob.getContent(), type);
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
        return fromBlob(User.class).apply(usersBucket().get(Prefix.EMAIL.key(email)));
    }

    @Override
    public User getUserById(long id)
    {
        return fromBlob(User.class).apply(usersBucket().get(Prefix.EMAIL.key(id)));
    }

    @Override
    public Collection<Long> getAllUserIds()
    {
        int prefixLength = Prefix.USERID.toString().length();
        return StreamSupport
                .stream(usersBucket()
                        .list(BlobListOption.prefix(Prefix.USERID.toString()))
                        .iterateAll()
                        .spliterator(), false)
                .map(Blob::getName)
                .map(s -> s.substring(prefixLength))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    @Override
    public SessionData getSessionByUserId(long id)
    {
        return Optional
                .ofNullable(fromBlob(SessionData.class)
                        .apply(usersBucket().get(Prefix.SESSIONUSER.key(id))))
                .filter(notExpired(SessionData::getExpiry))
                .orElse(null);
    }

    @Override
    public SessionData getSessionBySessionId(long id)
    {
        return Optional
                .ofNullable(fromBlob(SessionData.class)
                        .apply(usersBucket().get(Prefix.SESSIONID.key(id))))
                .filter(notExpired(SessionData::getExpiry))
                .orElse(null);
    }

    @Override
    public SessionData setSession(User user)
    {
        final long expiry = new Date().getTime() + TimeUnit.DAYS.toMillis(1);
        SessionData answer = getSessionByUserId(user.getId());
        if (answer == null)
        {
            answer = new SessionData(getSessionId(user), user.getId(), expiry);
        }
        else
        {
            answer.setExpiry(expiry);
        }
        Bucket bucket = usersBucket();
        byte[] data = toYaml(answer).getBytes();
        bucket.create(Prefix.SESSIONID.key(answer.getId()), data);
        bucket.create(Prefix.SESSIONUSER.key(answer.getUserId()), data);
        return answer;
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
        // Deletions not supported to avoid early deletion charges
        // long now = new Date().getTime();
        // Bucket bucket = usersBucket();
        // String[] keysToDelete = StreamUtils
        // .createStreamFromIterator(bucket.list().iterateAll().iterator())
        // .map(fromBlob(SessionData.class)::apply)
        // .filter(sd -> sd.getExpiry() < now)
        // .mapToLong(SessionData::getId)
        // .mapToObj(Prefix.SESSIONID::key).toArray(String[]::new);
        // bucket.
    }

    @Override
    public User createUser(User user)
    {
        long nextId = getAllUserIds().stream().max(Long::compare).orElse(-1L) + 1;
        user.setId(nextId);
        Bucket bucket = usersBucket();
        byte[] data = toYaml(user).getBytes();
        bucket.create(Prefix.USERID.key(nextId), data);
        bucket.create(Prefix.EMAIL.key(user.getEmail()), data);
        return user;
    }

    @Override
    public void updateUser(User user)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public PasswordReset generatePasswordReset(String email)
    {
        PasswordReset answer = null;
        final User user = getUserByEmail(email);
        if (user != null)
        {
            answer = new PasswordReset(user.getId(), email);
            usersBucket().create(Prefix.PWRESET.key(answer.getId()), toYaml(answer).getBytes());
        }
        return answer;
    }

    @Override
    public PasswordReset getPasswordReset(long id)
    {
        return Optional
                .ofNullable(fromBlob(PasswordReset.class)
                        .apply(usersBucket().get(Prefix.PWRESET.key(id))))
                .filter(notExpired(PasswordReset::getExpiryTime))
                .orElse(null);
    }

    private final <T> Predicate<T> notExpired(ToLongFunction<T> expiryGetter)
    {
        return obj -> {
            long expiry = expiryGetter.applyAsLong(obj);
            long now = new Date().getTime();
            boolean answer = expiry >= now;
            LOG.info("expiry: {}, now: {}, answer: {}", new Date(expiry), new Date(now), answer);
            return answer;
        };
    }

    @Override
    public void clearExpiredResets()
    {
        // Not for now

    }

    @Override
    public void deleteUser(long id)
    {
        // TODO Auto-generated method stub
        // Not for now
    }

}
