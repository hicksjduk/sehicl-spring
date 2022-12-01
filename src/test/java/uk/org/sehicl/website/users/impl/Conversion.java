package uk.org.sehicl.website.users.impl;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.User.Status;

public class Conversion
{
    private RedisDatastore redisStore = new RedisDatastore("ec2-34-206-245-231.compute-1.amazonaws.com",
            18679, "p79309e5ed766336755029b8a30eee99b234390f6a83b6040eba1bf4efde6ae91");
    
    public void test()
    {
        new Datastore().users.forEach(this::createOrUpdate);
    }
    
    private void createOrUpdate(User u)
    {
        if (u.getId() == null)
            redisStore.createUser(u);
        else
            redisStore.updateUser(u);
    }
    
    private static class Datastore
    {
        private final List<User> users;

        Datastore()
        {
            users = Arrays.asList(
                    new User(4L, "Shaun Harris", "shaunharris10@btinternet.com", "Gosport Borough",
                            Status.ACTIVE, 0, "Q2hhdHMxOTYw", false),
                    new User(5L, "Jeremy Hicks", "website@sehicl.org.uk", null, Status.ACTIVE, 0,
                            "d2NlYWcxZXM=", false),
                    new User("SADAD", "sarma.ashish111@gmail.com", "Curdridge", Status.ACTIVE, 0,
                            "d2VsbHdlbGw=", false),
                    new User(3L, "Richard Matthews", "matthews-richard3@sky.com", null, Status.ACTIVE, 0,
                            "UG9tcGV5MQ==", false),
                    new User(1L, "Andy Stretton", "andrewstretton@sky.com", "Portsmouth", Status.ACTIVE, 0,
                            "Z2FtZXNldCZtYXRjaA==", false),
                    new User(2L, "Daniel Oliver", "danieloliver@ntlworld.com", "Havant", Status.ACTIVE, 0,
                            "ZmVycmV0dA==", false),
                    new User(0L, "Andrew Kirby", "andrew.kirby21@gmail.com", "XIIth Men", Status.ACTIVE, 0,
                            "QzBzaGFtODM=", false));
        }
    }
}
