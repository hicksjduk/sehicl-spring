package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.PasswordReset;

public class PasswordResetMailTemplate extends Template<PasswordResetMail>
{
    public PasswordResetMailTemplate(PasswordReset reset, String resetPageAddress)
    {
        super("passwordResetMail.ftl", new PasswordResetMail(reset.getId(), resetPageAddress));
    }
}
