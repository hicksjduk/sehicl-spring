package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.User;

public class UserDetailsPage extends Page
{
    private final User user;

    public UserDetailsPage(User user)
    {
        super("userDetails", "userDetails.ftlh", null);
        this.user = user;
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
}
