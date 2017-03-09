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

    public TeamFixturesPage(String teamId, String uri)
    {
        super("fixtures", "teamfixtures.ftlh", Section.FIXTURES, uri);
        final Model model = ModelLoader.getModel();
        fixtures = new TeamFixtures.Builder(model, teamId, Completeness.CONSISTENT,
                new Rules.Builder().build()).build();
        title = String.format("Fixtures: %s", fixtures.getTeam().getName());
    }

    public TeamFixturesPage(String teamId, int season, String uri)
    {
        super("fixtures", "teamfixtures.ftlh", Section.ARCHIVE, uri);
        final Model model = ModelLoader.getModel(season);
        fixtures = new TeamFixtures.Builder(model, teamId, Completeness.COMPLETE,
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
