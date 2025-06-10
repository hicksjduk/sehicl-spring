package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.Reset;

public class ResetPage extends Page
{
    private final Reset reset;

    public ResetPage(Reset reset)
    {
        super("passwordReset", "passwordReset.ftlh", null);
        this.reset = reset;
    }

    @Override
    public String getTitle()
    {
        return "SEHICL Password Reset";
    }

    public Reset getReset()
    {
        return reset;
    }
}
