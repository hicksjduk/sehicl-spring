package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class AdminNotifyMail
{
    private final User user;
    private final String userDetailsPageAddress;

    public AdminNotifyMail(User user, String userDetailsPageAddress)
    {
        this.user = user;
        this.userDetailsPageAddress = userDetailsPageAddress;
    }

    public User getUser()
    {
        return user;
    }

    public String getUserDetailsPageAddress()
    {
        return userDetailsPageAddress;
    }
}
