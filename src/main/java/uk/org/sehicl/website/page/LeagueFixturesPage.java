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
    private final Integer season;

    public LeagueFixturesPage()
    {
        this(null, null);
    }

    public LeagueFixturesPage(String leagueId)
    {
        this(null, leagueId);
    }

    public LeagueFixturesPage(int season)
    {
        this(season, null);
    }

    public LeagueFixturesPage(Integer season, String leagueId)
    {
        super("fixtures", "leaguefixtures.ftlh", Section.FIXTURES);
        this.season = season;
        final Model model = season == null ? ModelLoader.getModel() : ModelLoader.getModel(season);
        fixtures = new LeagueFixtures.Builder(model, season, leagueId, Completeness.CONSISTENT,
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
