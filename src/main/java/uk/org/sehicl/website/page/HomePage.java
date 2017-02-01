package uk.org.sehicl.website.page;

import uk.org.sehicl.website.navigator.Section;

public class HomePage extends Page
{
    public HomePage(String uri)
    {
        super("home", "home.ftlh", Section.HOME, uri);
    }

    @Override
    public String getTitle()
    {
        return "South-East Hampshire (Fareham) Indoor Cricket League";
    }

}
