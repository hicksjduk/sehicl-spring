package uk.org.sehicl.website.page;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.TeamFixtures;
import uk.org.sehicl.website.rules.Rules;

public class TeamFixturesPage extends Page
{
    private final TeamFixtures fixtures;
    private final String title;

    public TeamFixturesPage(String teamId)
    {
        super("fixtures", "teamfixtures.ftlh", Section.FIXTURES);
        final Model model = ModelLoader.getModel();
        fixtures = new TeamFixtures.Builder(model, teamId, null, Completeness.CONSISTENT,
                new Rules.Builder().build()).build();
        title = String.format("Fixtures: %s", fixtures.getTeam().getName());
    }

    public TeamFixturesPage(String teamId, int season)
    {
        super("fixtures", "teamfixtures.ftlh", Section.ARCHIVE);
        final Model model = ModelLoader.getModel(season);
        fixtures = new TeamFixtures.Builder(model, teamId, season, Completeness.COMPLETE,
                new Rules.Builder(season).build()).build();
        title = String.format("Fixtures: %s (%d-%02d)", fixtures.getTeam().getName(), season + 1999,
                season);
    }

    public TeamFixtures getFixtures()
    {
        return fixtures;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

}
