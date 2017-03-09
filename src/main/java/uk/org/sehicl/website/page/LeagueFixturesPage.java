package uk.org.sehicl.website.page;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.LeagueFixtures;
import uk.org.sehicl.website.rules.Rules;

public class LeagueFixturesPage extends Page
{
    private final LeagueFixtures fixtures;
    private final String title;

    public LeagueFixturesPage(String uri)
    {
        this(null, uri);
    }

    public LeagueFixturesPage(String leagueId, String uri)
    {
        super("fixtures", "leaguefixtures.ftlh", Section.FIXTURES, uri);
        final Model model = ModelLoader.getModel();
        fixtures = new LeagueFixtures.Builder(model, leagueId, Completeness.CONSISTENT,
                new Rules.Builder().build()).build();
        title = leagueId == null ? "SEHICL Fixtures"
                : String.format("Fixtures: %s", fixtures.getLeague().getName());
    }

    public LeagueFixtures getFixtures()
    {
        return fixtures;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

}
