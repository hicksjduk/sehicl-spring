package uk.org.sehicl.website.users.impl;

import java.util.Collection;

import uk.org.sehicl.website.users.PasswordReset;
import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.User.Status;
import uk.org.sehicl.website.users.UserDatastore;

public class GoogleCloudDatastore implements UserDatastore
{

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
    public Collection<Long> getAllUserIds()
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
    public SessionData setSession(User user)
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateUser(User user)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public PasswordReset generatePasswordReset(String email)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PasswordReset getPasswordReset(long id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clearExpiredResets()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteUser(long id)
    {
        // TODO Auto-generated method stub

    }

}
