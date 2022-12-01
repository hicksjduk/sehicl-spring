package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.Login;
import uk.org.sehicl.website.users.UserManager;

public class LoginPage extends Page
{
    private final Login login;

    public LoginPage(String uri, UserManager userManager)
    {
        this(uri, new Login(userManager));
    }

    public LoginPage(String uri, Login login)
    {
        super("login", "login.ftlh", null, uri);
        this.login = login;
    }

    @Override
    public String getTitle()
    {
        return "SEHICL Login";
    }

    public Login getLogin()
    {
        return login;
    }
}
