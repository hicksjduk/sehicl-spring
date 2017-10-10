package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class ActivationMail
{
    private final User user;
    private final String activationPageAddress;

    public ActivationMail(User user, String activationPageAddress)
    {
        this.user = user;
        this.activationPageAddress = activationPageAddress;
    }

    public User getUser()
    {
        return user;
    }

    public String getActivationPageAddress()
    {
        return activationPageAddress;
    }
}
