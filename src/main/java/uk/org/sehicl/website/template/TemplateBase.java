package uk.org.sehicl.website.template;

import java.io.IOException;
import java.io.Writer;

public abstract class TemplateBase<T>
{
    private final String templateName;
    
    public TemplateBase(String templateName)
    {
        this.templateName = templateName;
    }

    public void process(T data, Writer writer)
    {
        freemarker.template.Template template;
        try
        {
            template = TemplateConfig.getConfiguration().getTemplate(templateName);
            template.process(data, writer);
        }
        catch (IOException | freemarker.template.TemplateException e)
        {
            throw new RuntimeException("Unable to process template", e);
        }
    }

}
