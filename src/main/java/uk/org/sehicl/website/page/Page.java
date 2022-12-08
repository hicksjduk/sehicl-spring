package uk.org.sehicl.website.page;

import uk.org.sehicl.website.navigator.Navigator;
import uk.org.sehicl.website.navigator.Section;

public abstract class Page
{
    private final String pageId;
    private final String contentTemplate;
    private final Navigator navigator;
    private final boolean blockIndexing;

    public Page(String pageId, String contentTemplate, Section section, String uri)
    {
        this.pageId = pageId;
        this.contentTemplate = contentTemplate;
        this.navigator = new Navigator(section, uri);
        this.blockIndexing = System.getenv("BLOCK_INDEXING") != null;
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

    public boolean isBlockIndexing()
    {
        return blockIndexing;
    }
}
