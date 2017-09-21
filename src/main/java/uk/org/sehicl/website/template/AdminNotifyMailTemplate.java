package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class AdminNotifyMailTemplate extends Template<User>
{
    public AdminNotifyMailTemplate(User user)
    {
        super("adminNotifyMail.ftl", user);
    }
}
