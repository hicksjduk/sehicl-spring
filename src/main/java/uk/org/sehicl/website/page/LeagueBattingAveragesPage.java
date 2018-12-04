package uk.org.sehicl.website.page;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.BattingAverages;
import uk.org.sehicl.website.report.LeagueSelector;
import uk.org.sehicl.website.report.ModelAndRules;

public class LeagueBattingAveragesPage extends Page
{
    private final BattingAverages averages;
    private final String title;
    private final boolean current;

    public LeagueBattingAveragesPage(LeagueSelector selector, String uri)
    {
        super("averages", "leaguebattingaverages.ftlh", Section.AVERAGES, uri);
        averages = new BattingAverages.Builder(selector, Completeness.CONSISTENT, 50,
                new ModelAndRules()).build();
        title = String.format("%s Batting", selector.getName());
        current = true;
    }

    public LeagueBattingAveragesPage(LeagueSelector selector, int season, String uri)
    {
        super("averages", "leaguebattingaverages.ftlh", Section.ARCHIVE, uri);
        averages = new BattingAverages.Builder(selector, Completeness.COMPLETE, 50,
                new ModelAndRules(season)).build();
        title = String.format("%s Batting - Season %d-%02d", selector.getName(), season + 1999,
                season);
        current = false;
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
}
