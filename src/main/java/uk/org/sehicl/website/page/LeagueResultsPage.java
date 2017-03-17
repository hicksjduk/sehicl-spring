package uk.org.sehicl.website.page;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.LeagueResults;
import uk.org.sehicl.website.rules.Rules;

public class LeagueResultsPage extends Page
{
    private final LeagueResults results;
    private final String title;

    public LeagueResultsPage(String leagueId, String uri)
    {
        super("results", "results.ftlh", Section.RESULTS, uri);
        results = new LeagueResults.Builder(ModelLoader.getModel(), leagueId, Completeness.CONSISTENT,
                new Rules.Builder().build()).build();
        title = String.format("Results: %s", results.getLeague().getName());
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    public LeagueResults getResults()
    {
        return results;
    }
}
