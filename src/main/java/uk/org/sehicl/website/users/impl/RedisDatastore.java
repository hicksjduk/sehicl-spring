package uk.org.sehicl.website.users.impl;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ScanOptions.ScanOptionsBuilder;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import uk.org.sehicl.website.users.PasswordReset;
import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.User.Status;
import uk.org.sehicl.website.users.UserDatastore;

public class RedisDatastore implements UserDatastore
{
    private final JedisConnectionFactory connectionFactory;
    private RedisAtomicLong keyGenerator;
    private RedisTemplate<String, String> template;
    private HashOperations<String, Object, Object> ops;

    public RedisDatastore()
    {
        this(createConnectionFactory());
    }

    public RedisDatastore(String uri)
    {
        this(createConnectionFactory(uri));
    }

    public RedisDatastore(String host, int port)
    {
        this(createConnectionFactory(host, port, null));
    }

    public RedisDatastore(String host, Integer port, String password)
    {
        this(createConnectionFactory(host, port, password));
    }

    private RedisDatastore(JedisConnectionFactory connectionFactory)
    {
        (this.connectionFactory = connectionFactory).afterPropertiesSet();
    }
    
    private void init() {
        keyGenerator = new RedisAtomicLong("counter", connectionFactory);
        template = createTemplate();
        ops = template.opsForHash();
    }

    private static JedisConnectionFactory createConnectionFactory()
    {
        final JedisConnectionFactory answer = new JedisConnectionFactory();
        answer.setUsePool(true);
        return answer;
    }

    private static JedisConnectionFactory createConnectionFactory(String uriString)
    {
        final JedisConnectionFactory answer = createConnectionFactory();
        if (uriString != null)
        {
            URI uri = URI.create(uriString);
            answer.setHostName(uri.getHost());
            answer.setPort(uri.getPort());
            String userInfo = uri.getUserInfo();
            if (!StringUtils.isEmpty(userInfo))
            {
                final String password = userInfo.split(":")[1];
                answer.setPassword(password);
            }
        }
        return answer;
    }

    private static JedisConnectionFactory createConnectionFactory(String host, Integer port,
            String password)
    {
        final JedisConnectionFactory answer = createConnectionFactory();
        if (host != null)
        {
            answer.setHostName(host);
        }
        if (port != null)
        {
            answer.setPort(port);
        }
        if (password != null)
        {
            answer.setPassword(password);
        }
        return answer;
    }

    private RedisTemplate<String, String> createTemplate()
    {
        return createTemplate(connectionFactory, String.class, String.class);
    }

    private static <K, V> RedisTemplate<K, V> createTemplate(
            JedisConnectionFactory connectionFactory, Class<K> keyType, Class<V> valueType)
    {
        final RedisTemplate<K, V> answer = new RedisTemplate<>();
        answer.setConnectionFactory(connectionFactory);
        answer.setKeySerializer(new StringRedisSerializer());
        answer.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        answer.setHashKeySerializer(new GenericToStringSerializer<>(Long.class));
        answer.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        answer.afterPropertiesSet();
        return answer;
    }

    @Override
    public User getUserByEmail(String email)
    {
        if (ops == null)
            init();
        final User answer = (User) ops.get("email", email);
        return answer;
    }

    @Override
    public User getUserById(long id)
    {
        if (ops == null)
            init();
        final User answer = (User) ops.get("user", id);
        return answer;
    }

    @Override
    public SessionData getSessionByUserId(long id)
    {
        if (ops == null)
            init();
        SessionData answer = null;
        answer = (SessionData) ops.get("usersession", id);
        return answer;
    }

    @Override
    public SessionData getSessionBySessionId(long id)
    {
        if (ops == null)
            init();
        SessionData answer = null;
        answer = (SessionData) ops.get("session", id);
        return answer;
    }

    @Override
    public SessionData setSession(User user)
    {
        if (ops == null)
            init();
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
        ops.put("session", answer.getId(), answer);
        ops.put("usersession", answer.getUserId(), answer);
        return answer;
    }

    long getSessionId(User user)
    {
        if (ops == null)
            init();
        long factor = 10000000000L;
        final long time = new Date().getTime();
        long answer = user.getId() * factor + time % factor;
        return answer;
    }

    @Override
    public void clearExpiredSessions()
    {
        if (ops == null)
            init();
        long now = new Date().getTime();
        List<SessionData> expiredSessions = new ArrayList<>();
        try (Cursor<Entry<Object, Object>> c = ops
                .scan("session", ScanOptions.scanOptions().build()))
        {
            c.forEachRemaining(e ->
            {
                final SessionData s = (SessionData) e.getValue();
                if (s.getExpiry() < now)
                {
                    expiredSessions.add(s);
                }
            });
        }
        if (!expiredSessions.isEmpty())
        {
            ops.delete("session", expiredSessions.stream().mapToLong(SessionData::getId).toArray());
            ops
                    .delete("usersession",
                            expiredSessions.stream().mapToLong(SessionData::getUserId).toArray());
        }
    }

    @Override
    public User createUser(User user)
    {
        if (ops == null)
            init();
        user.setId(keyGenerator.getAndIncrement());
        updateUser(user);
        return user;
    }

    @Override
    public void updateUser(User user)
    {
        if (ops == null)
            init();
        ops.put("user", user.getId(), user);
        ops.put("email", user.getEmail(), user);
    }

    @Override
    public PasswordReset generatePasswordReset(String email)
    {
        if (ops == null)
            init();
        PasswordReset answer = null;
        final User user = getUserByEmail(email);
        if (user != null)
        {
            answer = new PasswordReset(user.getId(), email);
            ops.put("reset", answer.getId(), answer);
        }
        return answer;
    }

    @Override
    public PasswordReset getPasswordReset(long id)
    {
        if (ops == null)
            init();
        PasswordReset answer = (PasswordReset) ops.get("reset", id);
        return answer;
    }

    @Override
    public void clearExpiredResets()
    {
        if (ops == null)
            init();
        long now = new Date().getTime();
        List<PasswordReset> expiredResets = new ArrayList<>();
        try (Cursor<Entry<Object, Object>> c = ops.scan("reset", ScanOptions.scanOptions().build()))
        {
            c.forEachRemaining(e ->
            {
                final PasswordReset r = (PasswordReset) e.getValue();
                if (r.getExpiry() < now)
                {
                    expiredResets.add(r);
                }
            });
        }
        if (!expiredResets.isEmpty())
        {
            ops.delete("reset", expiredResets.stream().mapToLong(PasswordReset::getId).toArray());
        }
    }

    @Override
    public Collection<Long> getAllUserIds()
    {
        if (ops == null)
            init();
        String bucket = "user";
        Collection<Long> answer = ops
                .keys(bucket)
                .stream()
                .map(Long.class::cast)
                .collect(Collectors.toSet());
        return answer;
    }

    @Override
    public void deleteUser(long id)
    {
        if (ops == null)
            init();
        User user = getUserById(id);
        if (user == null)
            return;
        ops.delete("user", id);
        ops.delete("email", user.getEmail());
    }

}
