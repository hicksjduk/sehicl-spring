package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.User;

public class DeleteUserPage extends Page
{
    private final User user;
    private final boolean confirmed;
    
    public DeleteUserPage(String uri, User user, boolean confirmed)
    {
        super("deleteUser", "deleteUserConfirm.ftlh", null, uri);
        this.user = user;
        this.confirmed = confirmed;
    }

    @Override
    public String getTitle()
    {
        return "SEHICL User Details";
    }

    public User getUser()
    {
        return user;
    }

    public boolean isConfirmed()
    {
        return confirmed;
    }
}
