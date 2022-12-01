package uk.org.sehicl.website.report;

import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.rules.Rules;

public class ModelAndRules
{
    public final Model model;
    public final Rules rules;
    
    public ModelAndRules()
    {
        this(ModelLoader.getModel(), new Rules.Builder().build());
    }

    public ModelAndRules(int season)
    {
        this(ModelLoader.getModel(season), new Rules.Builder(season).build());
    }

    public ModelAndRules(Model model, Rules rules)
    {
        this.model = model;
        this.rules = rules;
    }
}
