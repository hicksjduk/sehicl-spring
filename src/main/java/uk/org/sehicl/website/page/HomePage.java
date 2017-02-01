package uk.org.sehicl.website.page;

import uk.org.sehicl.website.navigator.Section;

public class HomePage extends Page
{
    public HomePage(Section section, String uri)
    {
        super("home", "home.html", section, uri);
    }

    @Override
    public String getTitle()
    {
        return "South-East Hampshire (Fareham) Indoor Cricket League";
    }

}
