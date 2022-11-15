package uk.org.sehicl.website.users;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PasswordReset
{
    private long userId;
    private String userEmail;
    private long id;
    private long expiryTime;
    
    @SuppressWarnings("unused")
    private PasswordReset()
    {
    }
    
    public PasswordReset(long userId, String userEmail)
    {
        long factor = 100000000000L;
        setUserId(userId);
        setUserEmail(userEmail);
        final long time = new Date().getTime();
        setId(userId * factor + time % factor);
        setExpiryTime(time + TimeUnit.HOURS.toMillis(3));
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setExpiryTime(long expiryTime)
    {
        this.expiryTime = expiryTime;
    }

    public long getUserId()
    {
        return userId;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public long getId()
    {
        return id;
    }

    public long getExpiry()
    {
        return expiryTime;
    }
}
