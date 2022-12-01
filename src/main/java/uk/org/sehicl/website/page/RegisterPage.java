package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.Register;
import uk.org.sehicl.website.users.UserManager;

public class RegisterPage extends Page
{
    private final Register register;

    public RegisterPage(String uri, UserManager userManager)
    {
        this(uri, new Register(userManager));
    }

    public RegisterPage(String uri, Register register)
    {
        super("register", "register.ftlh", null, uri);
        this.register = register;
    }

    @Override
    public String getTitle()
    {
        return "SEHICL User Registration";
    }

    public Register getRegister()
    {
        return register;
    }
}
