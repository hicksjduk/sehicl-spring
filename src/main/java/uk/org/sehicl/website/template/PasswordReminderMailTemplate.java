package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class PasswordReminderMailTemplate extends Template<User>
{
    public PasswordReminderMailTemplate(User user)
    {
        super("passwordReminderMail.ftl", user);
    }
}
