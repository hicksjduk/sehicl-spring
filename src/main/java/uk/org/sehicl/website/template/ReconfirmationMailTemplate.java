package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class ReconfirmationMailTemplate extends Template<ReconfirmationMail>
{
    public ReconfirmationMailTemplate(User user, String reconfirmationPageAddress)
    {
        super("reconfirmationMail.ftlh", new ReconfirmationMail(user, reconfirmationPageAddress));
    }
}
