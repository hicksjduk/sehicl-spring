package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class ActivationMailTemplate extends Template<ActivationMail>
{
    public ActivationMailTemplate(User user, String serverAddress)
    {
        super("activationMail.ftlh", new ActivationMail(user, serverAddress));
    }
}
