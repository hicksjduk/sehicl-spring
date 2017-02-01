package uk.org.sehicl.website.template;

import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;

public class TemplateConfig
{
    private static Configuration CONFIG = null;

    public static synchronized Configuration getConfiguration()
    {
        if (CONFIG == null)
        {
            Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            cfg
                    .setClassLoaderForTemplateLoading(TemplateConfig.class.getClassLoader(),
                            "templates");
            CONFIG = cfg;
        }
        return CONFIG;
    }
}
