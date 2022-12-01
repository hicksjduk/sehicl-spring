package uk.org.sehicl.website.template;

import uk.org.sehicl.website.users.User;

public class AdminNotifyMailTemplate extends Template<AdminNotifyMail>
{
    public AdminNotifyMailTemplate(User user, String serverAddress)
    {
        super("adminNotifyMail.ftlh", new AdminNotifyMail(user, serverAddress));
    }
}
