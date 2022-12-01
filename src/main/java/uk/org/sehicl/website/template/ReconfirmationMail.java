package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class ReconfirmationMail
{
    private final User user;
    private final String reconfirmationPageAddress;

    public ReconfirmationMail(User user, String reconfirmationPageAddress)
    {
        this.user = user;
        this.reconfirmationPageAddress = reconfirmationPageAddress;
    }

    public User getUser()
    {
        return user;
    }

    public String getReconfirmationPageAddress()
    {
        return reconfirmationPageAddress;
    }
}
