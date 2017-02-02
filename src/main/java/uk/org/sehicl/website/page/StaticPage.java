package uk.org.sehicl.website.page;

import uk.org.sehicl.website.navigator.Section;

public class StaticPage extends Page
{
    private final String title;

    public StaticPage(String pageId, String contentTemplate, Section section, String uri, String title)
    {
        super(pageId, contentTemplate, section, uri);
        this.title = title;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

}
