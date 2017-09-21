package uk.org.sehicl.website.users;

public class SessionData
{
    public long id;
    public long userId;
    public long expiry;

    public SessionData()
    {
    }

    public SessionData(long id, long userId, long expiry)
    {
        this.id = id;
        this.userId = userId;
        this.expiry = expiry;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public long getExpiry()
    {
        return expiry;
    }

    public void setExpiry(long expiry)
    {
        this.expiry = expiry;
    }
}