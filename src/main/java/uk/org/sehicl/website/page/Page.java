package uk.org.sehicl.website.page;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import uk.org.sehicl.website.EnvVar;
import uk.org.sehicl.website.navigator.Navigator;
import uk.org.sehicl.website.navigator.Section;

public abstract class Page
{
    private final String pageId;
    private final String contentTemplate;
    private final Navigator navigator;
    private final boolean blockIndexing;

    public Page(String pageId, String contentTemplate, Section section)
    {
        var uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        this.pageId = pageId;
        this.contentTemplate = contentTemplate;
        this.navigator = new Navigator(section, uri);
        this.blockIndexing = EnvVar.BLOCK_INDEXING.get().map(Boolean::valueOf).orElse(false);
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
