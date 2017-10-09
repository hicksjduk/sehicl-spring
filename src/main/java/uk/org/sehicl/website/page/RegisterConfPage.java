package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.Register;
import uk.org.sehicl.website.users.UserManager;

public class RegisterConfPage extends Page
{
    private final Register register;

    public RegisterConfPage(String uri, UserManager userManager)
    {
        this(uri, new Register(userManager));
    }

    public RegisterConfPage(String uri, Register register)
    {
        super("register", "registerConf.ftlh", null, uri);
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
