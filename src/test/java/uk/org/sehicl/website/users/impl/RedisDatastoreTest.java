package uk.org.sehicl.website.users.impl;

import org.junit.Test;

import uk.org.sehicl.website.users.User.Status;

public class RedisDatastoreTest
{

    @Test
    public void testCreateUser()
    {
        new RedisDatastore().createUser("a", "b", "c", Status.INACTIVE, "d");
    }

}
