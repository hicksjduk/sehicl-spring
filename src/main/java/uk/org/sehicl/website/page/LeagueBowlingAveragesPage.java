package uk.org.sehicl.website.page;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.BowlingAverages;
import uk.org.sehicl.website.report.LeagueSelector;
import uk.org.sehicl.website.report.ModelAndRules;

public class LeagueBowlingAveragesPage extends Page
{
    private final BowlingAverages averages;
    private final String title;
    private final boolean current;

    public LeagueBowlingAveragesPage(LeagueSelector selector, String uri)
    {
        super("averages", "leaguebowlingaverages.ftlh", Section.AVERAGES, uri);
        averages = new BowlingAverages.Builder(selector, Completeness.CONSISTENT, 50,
                new ModelAndRules()).build();
        title = String.format("%s Bowling", selector.getName());
        current = true;
    }

    public LeagueBowlingAveragesPage(LeagueSelector selector, int season, String uri)
    {
        super("averages", "leaguebowlingaverages.ftlh", Section.ARCHIVE, uri);
        averages = new BowlingAverages.Builder(selector, Completeness.COMPLETE, 50,
                new ModelAndRules(season)).build();
        title = String.format("%s Bowling - Season %d-%02d", selector.getName(), season + 1999,
                season);
        current = false;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    public BowlingAverages getAverages()
    {
        return averages;
    }

    public boolean isCurrent()
    {
        return current;
    }
}
