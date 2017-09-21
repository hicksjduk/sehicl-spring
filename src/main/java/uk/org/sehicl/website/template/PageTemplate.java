package uk.org.sehicl.website.template;

import uk.org.sehicl.website.page.Page;

public class PageTemplate extends Template<Page>
{
    public PageTemplate(Page page)
    {
        super("page.ftlh", page);
    }
}
