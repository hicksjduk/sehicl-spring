package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.User;

public class ActivatePage extends Page
{
    private final User user;

    public ActivatePage(String uri, User user)
    {
        super("activate", "activate.ftlh", null, uri);
        this.user = user;
    }

    @Override
    public String getTitle()
    {
        return "SEHICL User Activation";
    }

    public User getUser()
    {
        return user;
    }
}
