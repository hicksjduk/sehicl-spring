package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.Register;
import uk.org.sehicl.website.users.UserManager;

public class RegisterPage extends Page
{
    private final Register register;

    public RegisterPage(UserManager userManager)
    {
        this(new Register(userManager));
    }

    public RegisterPage(Register register)
    {
        super("register", "register.ftlh", null);
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
