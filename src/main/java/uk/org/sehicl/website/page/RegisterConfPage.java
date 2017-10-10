package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.User;

public class RegisterConfPage extends Page
{
    private final User user;

    public RegisterConfPage(String uri, User user)
    {
        super("register", "registerConf.ftlh", null, uri);
        this.user = user;
    }

    @Override
    public String getTitle()
    {
        return "SEHICL User Registration";
    }

    public User getUser()
    {
        return user;
    }
}
