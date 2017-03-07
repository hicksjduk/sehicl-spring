package uk.org.sehicl.website.page;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.BattingAverages;
import uk.org.sehicl.website.report.LeagueSelector;
import uk.org.sehicl.website.rules.Rules;

public class LeagueBattingAveragesPage extends Page
{
    private final BattingAverages averages;
    private final String title;
    private final boolean current;
    private final LeagueSelector selector;

    public LeagueBattingAveragesPage(LeagueSelector selector, String uri)
    {
        super("averages", "leaguebattingaverages.ftlh", Section.AVERAGES, uri);
        this.selector = selector;
        final Model model = ModelLoader.getModel();
        averages = new BattingAverages.Builder(model, selector, 
                Completeness.CONSISTENT, new Rules.Builder().build(), 50).build();
        title = String.format("%s Batting", selector.getName());
        current = true;
    }

    public LeagueBattingAveragesPage(LeagueSelector selector, int season, String uri)
    {
        super("averages", "leaguebattingaverages.ftlh", Section.AVERAGES, uri);
        this.selector = selector;
        final Model model = ModelLoader.getModel();
        averages = new BattingAverages.Builder(model, selector, 
                Completeness.CONSISTENT, new Rules.Builder().build(), 50).build();
        title = String.format("%s Batting - Season %d-%02d", selector.getName(), season + 1999, season);
        current = true;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    public BattingAverages getAverages()
    {
        return averages;
    }

    public boolean isCurrent()
    {
        return current;
    }

    public String getPageHeading()
    {
        return String.format("%s Batting", selector.getName());
    }
}
