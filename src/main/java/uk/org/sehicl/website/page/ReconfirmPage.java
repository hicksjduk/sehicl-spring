package uk.org.sehicl.website.page;

import uk.org.sehicl.website.users.Reconfirm;

public class ReconfirmPage extends Page
{
    private final Reconfirm reconfirm;

    public ReconfirmPage(Reconfirm reconfirm)
    {
        super("reconfirm", "reconfirm.ftlh", null);
        this.reconfirm = reconfirm;
    }

    @Override
    public String getTitle()
    {
        return "SEHICL User Reconfirmation";
    }

    public Reconfirm getReconfirm()
    {
        return reconfirm;
    }
}
