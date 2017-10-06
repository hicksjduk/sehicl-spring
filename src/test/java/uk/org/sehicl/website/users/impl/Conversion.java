package uk.org.sehicl.website.users.impl;

public class Conversion
{
    public void test()
    {
        LocalDatabaseUserDatastore dbStore = new LocalDatabaseUserDatastore(
                "/Users/jerhicks/Documents/personal/sehicl/users.db");
        RedisDatastore redisStore = new RedisDatastore("ec2-34-206-245-231.compute-1.amazonaws.com", 18679, "p79309e5ed766336755029b8a30eee99b234390f6a83b6040eba1bf4efde6ae91");
        dbStore.getUserKeys().stream().map(dbStore::getUserById).forEach(redisStore::createUser);
    }

}
