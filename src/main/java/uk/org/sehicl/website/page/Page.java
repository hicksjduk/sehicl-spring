package uk.org.sehicl.website.page;

import java.util.Optional;

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
        this.blockIndexing = Optional
                .of("BLOCK_INDEXING")
                .map(System::getenv)
                .map(Boolean::valueOf)
                .orElse(false);
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
