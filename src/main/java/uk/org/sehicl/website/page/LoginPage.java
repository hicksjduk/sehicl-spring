package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.Login;
import uk.org.sehicl.website.users.UserManager;

public class LoginPage extends Page
{
    private final Login login;

    public LoginPage(UserManager userManager)
    {
        this(new Login(userManager));
    }

    public LoginPage(Login login)
    {
        super("login", "login.ftlh", null);
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
