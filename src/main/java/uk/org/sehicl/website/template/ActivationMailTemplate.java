package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class ActivationMailTemplate extends Template<User>
{
    public ActivationMailTemplate(User user)
    {
        super("activationMail.ftl", user);
    }
}
