package uk.org.sehicl.website.page;

import uk.org.sehicl.website.navigator.Navigator;
import uk.org.sehicl.website.navigator.Section;

public abstract class Page
{
    private final String pageId;
    private final String contentTemplate;
    private final Navigator navigator;

    public Page(String pageId, String contentTemplate, Section section, String uri)
    {
        this.pageId = pageId;
        this.contentTemplate = contentTemplate;
        this.navigator = new Navigator(section, uri);
    }

    public abstract String getTitle();

    public String getPageId()
    {
        return pageId;
    }

    public String getContentTemplate()
    {
        return contentTemplate;
    }

    public Navigator getNavigator()
    {
        return navigator;
    }

    public String getContactsLink()
    {
        return new MailtoLink.Builder("contact")
                .build()
                .toString();
    }

    public String getFixtureSecretaryLink()
    {
        return new MailtoLink.Builder("fixturesec")
                .setDescription("SEHICL Fixture Secretary")
                .setLinkText("the Fixture Secretary")
                .build()
                .toString();
    }

    public String getSecretaryLink()
    {
        return new MailtoLink.Builder("secretary")
                .setDescription("SEHICL Secretary")
                .setLinkText("the Secretary")
                .build()
                .toString();
    }

    public String getWebmasterLink()
    {
        return new MailtoLink.Builder("website")
                .setDescription("SEHICL Webmaster")
                .setLinkText("the Webmaster")
                .build()
                .toString();
    }
}
