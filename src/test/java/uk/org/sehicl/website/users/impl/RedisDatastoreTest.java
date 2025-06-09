package uk.org.sehicl.website.users.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import redis.embedded.RedisServer;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.User.Status;
import uk.org.sehicl.website.users.impl.RedisDatastore.Bucket;

public class RedisDatastoreTest
{
    private static RedisServer server;

    private final RedisDatastore datastore = new RedisDatastore("redis://localhost:6378");

    @BeforeAll
    static void startServer() throws Exception
    {
        server = new RedisServer(6378);
        server.start();
    }

    @AfterAll
    static void stopServer() throws Exception
    {
        server.stop();
    }

    @BeforeEach
    void clearDb()
    {
        try (var conn = datastore.connect())
        {
            conn.flushAll();
        }
    }

    void checkTtl(Bucket bucket, String key, long expected, long tolerance)
    {
        List<Long> ttl;
        try (var conn = datastore.connect())
        {
            try
            {
                ttl = conn.httl(bucket.toString(), key);
            }
            catch (Exception ex)
            {
                return;
            }
            assertThat(ttl.size()).isEqualTo(1);
            assertThat(ttl.get(0)).isCloseTo(expected, within(tolerance));
        }
    }

    void checkTtl(Bucket bucket, long key, long expected, long tolerance)
    {
        checkTtl(bucket, RedisDatastore.toStringKey(key), expected, tolerance);
    }

    public void testGetUserIds()
    {
        assertThat(datastore.getAllUserIds()).isEmpty();
    }

    @Test
    public void testCreateUser()
    {
        var created = datastore.createUser("a", "b", "c", Status.INACTIVE, "d");
        assertThat(created).isEqualTo(datastore.getUserById(created.getId()));
        assertThat(created).isEqualTo(datastore.getUserByEmail(created.getEmail()));
    }

    @Test
    public void testUpdateUser()
    {
        var user = datastore.createUser("X", "Y", "Z", Status.INACTIVE, "afafas");
        user.setStatus(Status.ACTIVE);
        datastore.updateUser(user);
        assertThat(user).isEqualTo(datastore.getUserById(user.getId()));
        assertThat(user).isEqualTo(datastore.getUserByEmail(user.getEmail()));
    }

    @Test
    public void testGetUserByEmail()
    {
        var email = "hahah";
        var name = "sshsdh";
        datastore.createUser(email, name, "asdgsadg", Status.INACTIVE, "asfdafgag");
        var user = datastore.getUserByEmail(email);
        assertThat(user.getName()).isEqualTo(name);
    }

    @Test
    public void testGetUserById()
    {
        var email = "ag98gas";
        var name = "aagsaas";
        var id = datastore
                .createUser(email, name, "agadhadh", Status.INACTIVE, "asdaghdhd")
                .getId();
        var user = datastore.getUserById(id);
        assertThat(user.getName()).isEqualTo(name);
    }

    @Test
    public void testSetSessionNoSessionForUser()
    {
        var user = new User(4L, "fa", "sgsd", "asaf", Status.ACTIVE, 4, "afsafas", true);
        var session = datastore.setSession(user);
        assertThat(session.getUserId()).isEqualTo(user.getId());
        checkTtl(Bucket.SESSION_BY_SESSION_ID, session.getId(), TimeUnit.DAYS.toSeconds(1), 5L);
        checkTtl(Bucket.SESSION_BY_USER_ID, session.getUserId(), TimeUnit.DAYS.toSeconds(1), 5L);
        assertThat(datastore.getSessionBySessionId(session.getId())).isNotNull();
        assertThat(datastore.getSessionByUserId(session.getUserId())).isNotNull();
    }

    @Test
    public void testSetSessionSessionForUser()
    {
        var user = new User(4L, "fa", "sgsd", "asaf", Status.ACTIVE, 4, "afsafas", true);
        datastore.setSession(user);
        var session = datastore.setSession(user);
        assertThat(session.getUserId()).isEqualTo(user.getId());
        checkTtl(Bucket.SESSION_BY_SESSION_ID, session.getId(), TimeUnit.DAYS.toSeconds(1), 5L);
        checkTtl(Bucket.SESSION_BY_USER_ID, session.getUserId(), TimeUnit.DAYS.toSeconds(1), 5L);
        assertThat(datastore.getSessionBySessionId(session.getId())).isNotNull();
        assertThat(datastore.getSessionByUserId(session.getUserId())).isNotNull();
    }

    @Test
    public void testSessionExpiry()
    {
        var user = new User(5L, "fa", "sgsd", "asaf", Status.ACTIVE, 4, "afsafas", true);
        var session = datastore.setSession(user, 0);
        datastore.clearExpiredSessions();
        assertThat(datastore.getSessionBySessionId(session.getId())).isNull();
        assertThat(datastore.getSessionByUserId(session.getUserId())).isNull();
    }

    @Test
    public void testGeneratePasswordResetUserDoesNotExist()
    {
        var user = new User(6L, "fa", "sgsd", "asaf", Status.ACTIVE, 4, "afsafas", true);
        var reset = datastore.generatePasswordReset(user.getEmail());
        assertThat(reset).isNull();
        datastore.clearExpiredResets();
        assertThat(datastore.getPasswordReset(user.getId())).isNull();
    }

    @Test
    public void testGeneratePasswordResetUserExists()
    {
        var user = new User(7L, "fa", "sgsd", "asaf", Status.ACTIVE, 4, "afsafas", true);
        datastore.createUser(user);
        var reset = datastore.generatePasswordReset(user.getEmail());
        assertThat(reset).isNotNull();
        datastore.clearExpiredResets();
        assertThat(datastore.getPasswordReset(user.getId())).isNotNull();
    }

    @Test
    public void testPasswordResetExpiry()
    {
        var user = new User(7L, "fa", "sgsd", "asaf", Status.ACTIVE, 4, "afsafas", true);
        datastore.createUser(user);
        assertThat(datastore.generatePasswordReset(user.getEmail(), 0L)).isNotNull();
        datastore.clearExpiredResets();
        assertThat(datastore.getPasswordReset(user.getId())).isNull();
    }
}
