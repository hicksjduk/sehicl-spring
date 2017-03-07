package uk.org.sehicl.website.page;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.BowlingAverages;
import uk.org.sehicl.website.report.LeagueSelector;
import uk.org.sehicl.website.rules.Rules;

public class LeagueBowlingAveragesPage extends Page
{
    private final BowlingAverages averages;
    private final String title;
    private final boolean current;
    private final LeagueSelector selector;

    public LeagueBowlingAveragesPage(LeagueSelector selector, String uri)
    {
        super("averages", "leaguebowlingaverages.ftlh", Section.AVERAGES, uri);
        this.selector = selector;
        final Model model = ModelLoader.getModel();
        averages = new BowlingAverages.Builder(model, selector, Completeness.CONSISTENT,
                new Rules.Builder().build(), 50).build();
        title = String.format("%s Bowling", selector.getName());
        current = true;
    }

    public LeagueBowlingAveragesPage(LeagueSelector selector, int season, String uri)
    {
        super("averages", "leaguebowlingaverages.ftlh", Section.ARCHIVE, uri);
        this.selector = selector;
        final Model model = ModelLoader.getModel(season);
        averages = new BowlingAverages.Builder(model, selector, Completeness.COMPLETE,
                new Rules.Builder(season).build(), 50).build();
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
