package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class ActivationMailTemplate extends Template<ActivationMail>
{
    public ActivationMailTemplate(User user, String activationPageAddress)
    {
        super("activationMail.ftl", new ActivationMail(user, activationPageAddress));
    }
}
