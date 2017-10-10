package uk.org.sehicl.website.users.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.User.Status;

public class RedisDatastoreTest
{
    private final RedisDatastore datastore = new RedisDatastore();

    @Test
    public void testCreateUser()
    {
        final User created = datastore.createUser("a", "b", "c", Status.INACTIVE, "d");
        assertThat(created).isEqualTo(datastore.getUserById(created.getId()));
        assertThat(created).isEqualTo(datastore.getUserByEmail(created.getEmail()));
    }

    @Test
    public void testUpdateUser()
    {
        final User user = datastore.createUser("X", "Y", "Z", Status.INACTIVE, "afafas");
        user.setStatus(Status.ACTIVE);
        datastore.updateUser(user);
        assertThat(user).isEqualTo(datastore.getUserById(user.getId()));
        assertThat(user).isEqualTo(datastore.getUserByEmail(user.getEmail()));
    }

    @Test
    public void testGetUserByEmail()
    {
        final String email = "hahah";
        final String name = "sshsdh";
        datastore.createUser(email, name, "asdgsadg", Status.INACTIVE, "asfdafgag");
        final User user = datastore.getUserByEmail(email);
        assertThat(user.getName()).isEqualTo(name);
    }

    @Test
    public void testGetUserById()
    {
        final String email = "ag98gas";
        final String name = "aagsaas";
        final long id = datastore
                .createUser(email, name, "agadhadh", Status.INACTIVE, "asdaghdhd")
                .getId();
        final User user = datastore.getUserById(id);
        assertThat(user.getName()).isEqualTo(name);
    }

    @Test
    public void testSetSessionNoSessionForUser()
    {
        final User user = new User(4L, "fa", "sgsd", "asaf", Status.ACTIVE, 4, "afsafas", true);
        final SessionData session = datastore.setSession(user);
        assertThat(session.getUserId()).isEqualTo(user.getId());
        assertThat(session.getExpiry()).isCloseTo(new Date().getTime() + TimeUnit.DAYS.toMillis(1),
                within(50L));
    }

    @Test
    public void testSetSessionSessionForUser()
    {
        final User user = new User(4L, "fa", "sgsd", "asaf", Status.ACTIVE, 4, "afsafas", true);
        datastore.setSession(user);
        final SessionData session = datastore.setSession(user);
        assertThat(session.getUserId()).isEqualTo(user.getId());
        assertThat(session.getExpiry()).isCloseTo(new Date().getTime() + TimeUnit.DAYS.toMillis(1),
                within(50L));
    }
    
    @Test
    public void testClearExpiredSessions()
    {
        datastore.clearExpiredSessions();
    }
    
    @Test
    public void testGetSessionId()
    {
        User user = new User();
        user.setId(14141L);
        final long result = datastore.getSessionId(user);
    }
}
