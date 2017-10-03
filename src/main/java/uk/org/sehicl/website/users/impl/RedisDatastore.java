package uk.org.sehicl.website.users.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.User.Status;
import uk.org.sehicl.website.users.UserDatastore;

public class RedisDatastore implements UserDatastore
{
    private final JedisConnectionFactory connectionFactory;
    private final RedisAtomicLong keyGenerator;

    public RedisDatastore()
    {
        (connectionFactory = createConnectionFactory()).afterPropertiesSet();;
        keyGenerator = new RedisAtomicLong("counter", connectionFactory, 0L);
    }

    public RedisDatastore(String host, int port)
    {
        (connectionFactory = createConnectionFactory(host, port)).afterPropertiesSet();;
        keyGenerator = new RedisAtomicLong("counter", connectionFactory);
    }

    private static JedisConnectionFactory createConnectionFactory()
    {
        final JedisConnectionFactory answer = new JedisConnectionFactory();
        answer.setUsePool(true);
        return answer;
    }

    private static JedisConnectionFactory createConnectionFactory(String host, int port)
    {
        final JedisConnectionFactory answer = createConnectionFactory();
        answer.setHostName(host);
        answer.setPort(port);
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
        answer.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        answer.afterPropertiesSet();
        return answer;
    }

    @Override
    public User getUserByEmail(String email)
    {
        final User answer = (User) createTemplate().opsForHash().get("email", email);
        return answer;
    }

    @Override
    public User getUserById(long id)
    {
        final User answer = (User) createTemplate().opsForHash().get("user", id);
        return answer;
    }

    @Override
    public SessionData getSessionByUserId(long id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SessionData getSessionBySessionId(long id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SessionData setSession(User user)
    {
        final HashOperations<String, Object, Object> ops = createTemplate().opsForHash();
        final long expiry = new Date().getTime() + TimeUnit.DAYS.toMillis(1);
        SessionData answer = getSessionByUserId(user.getId());
        if (answer == null)
        {
            answer = new SessionData(keyGenerator.incrementAndGet(), user.getId(), expiry);
        }
        else
        {
            answer.setExpiry(expiry);
        }
        ops.put("session", answer.getId(), answer);
        ops.put("usersession", answer.getUserId(), answer);
        return answer;
    }

    @Override
    public void clearExpiredSessions()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public User createUser(String email, String name, String club, Status status, String password)
    {
        User answer = new User(name, email, club, status, 0, password, true);
        answer.setId(keyGenerator.getAndIncrement());
        updateUser(answer);
        return answer;
    }

    @Override
    public void updateUser(User user)
    {
        final HashOperations<String, Object, Object> ops = createTemplate().opsForHash();
        ops.put("user", user.getId(), user);
        ops.put("email", user.getEmail(), user);
    }
}
