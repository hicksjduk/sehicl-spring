package uk.org.sehicl.website.template;

import java.io.IOException;
import java.io.Writer;

import uk.org.sehicl.website.page.Page;

public class Template
{
    private final Page page;
    
    public Template(Page page)
    {
        this.page = page;
    }

    public void process(Writer writer)
    {
        freemarker.template.Template template;
        try
        {
            template = TemplateConfig.getConfiguration().getTemplate("page.html");
            template.process(page, writer);
        }
        catch (IOException | freemarker.template.TemplateException e)
        {
            throw new RuntimeException("Unable to process template", e);
        }
    }

}
