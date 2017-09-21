package uk.org.sehicl.website.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public abstract class Template<T>
{
    private final String templateFile;
    private final T dataObj;
    
    public Template(String templateFile, T dataObj)
    {
        this.templateFile = templateFile;
        this.dataObj = dataObj;
    }

    public String process()
    {
        StringWriter sw = new StringWriter();
        process(sw);
        return sw.toString();
    }

    public void process(Writer writer)
    {
        freemarker.template.Template template;
        try
        {
            template = TemplateConfig.getConfiguration().getTemplate(templateFile);
            template.process(dataObj, writer);
        }
        catch (IOException | freemarker.template.TemplateException e)
        {
            throw new RuntimeException("Unable to process template", e);
        }
    }

}
