package uk.org.sehicl.website.page;

import uk.org.sehicl.website.navigator.Section;

public class HomePage extends Page
{
    public HomePage()
    {
        super("home", "home.ftlh", Section.HOME);
    }

    @Override
    public String getTitle()
    {
        return "South-East Hampshire (Fareham) Indoor Cricket League";
    }

}
