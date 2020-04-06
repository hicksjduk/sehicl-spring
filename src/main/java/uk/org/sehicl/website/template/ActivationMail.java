package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class ActivationMail
{
    private final User user;
    private final String serverAddress;

    public ActivationMail(User user, String serverAddress)
    {
        this.user = user;
        this.serverAddress = serverAddress;
    }

    public User getUser()
    {
        return user;
    }

    public String getServerAddress()
    {
        return serverAddress;
    }
}
