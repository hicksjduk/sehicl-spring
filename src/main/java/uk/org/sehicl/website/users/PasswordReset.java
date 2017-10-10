package uk.org.sehicl.website.users;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PasswordReset
{
    private final long userId;
    private final String userEmail;
    private final long id;
    private final long expiryTime;

    public PasswordReset(long userId, String userEmail)
    {
        long factor = 100000000000L;
        this.userId = userId;
        this.userEmail = userEmail;
        final long time = new Date().getTime();
        this.id = userId * factor + time % factor;
        this.expiryTime = time + TimeUnit.HOURS.toMillis(3);
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

    public long getExpiryTime()
    {
        return expiryTime;
    }
}
