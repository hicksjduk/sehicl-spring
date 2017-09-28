package uk.org.sehicl.website.users.impl;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.User.Status;
import uk.org.sehicl.website.users.UserDatastore;

public class RedisDatastore implements UserDatastore
{
    private final JedisConnectionFactory connectionFactory;

    public RedisDatastore()
    {
        connectionFactory = createConnectionFactory();
    }

    private static JedisConnectionFactory createConnectionFactory()
    {
        final JedisConnectionFactory answer = new JedisConnectionFactory();
        answer.setHostName("10.209.114.129");
        answer.setPort(6379);
        answer.setUsePool(true);
        answer.afterPropertiesSet();
        return answer;
    }

    private static <K, V> RedisTemplate<K, V> createTemplate(
            JedisConnectionFactory connectionFactory, Class<K> keyType, Class<V> valueType)
    {
        final RedisTemplate<K, V> answer = new RedisTemplate<>();
        answer.setConnectionFactory(connectionFactory);
        answer.afterPropertiesSet();
        return answer;
    }

    @Override
    public User getUserByEmail(String email)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User getUserById(long id)
    {
        // TODO Auto-generated method stub
        return null;
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
    public SessionData createSession(User user)
    {
        // TODO Auto-generated method stub
        return null;
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
        answer.setId(getNextCounterValue());
        final HashOperations<String, Object, Object> ops = createTemplate(connectionFactory, String.class, String.class).opsForHash();
//        ops.set(String.format("user:%d"), answer);
        return answer;
    }

    @Override
    public void updateUser(User user)
    {
        // TODO Auto-generated method stub

    }

    private final long getNextCounterValue()
    {
        final ValueOperations<String, Long> ops = createTemplate(connectionFactory, String.class,
                Long.class).opsForValue();
        final String key = "counter";
        ops.setIfAbsent(key, 0L);
        long answer = ops.increment(key, 1L);
        return answer;
    }
}
