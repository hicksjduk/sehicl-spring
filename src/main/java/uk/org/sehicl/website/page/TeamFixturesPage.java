package uk.org.sehicl.website.page;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.TeamFixtures;
import uk.org.sehicl.website.rules.Rules;

public class TeamFixturesPage extends Page
{
    private final TeamFixtures fixtures;
    private final String title;

    public TeamFixturesPage(Model model, String teamId, Completeness completenessThreshold,
            Rules rules, String uri)
    {
        super("fixtures", "teamfixtures.ftlh", Section.FIXTURES, uri);
        fixtures = new TeamFixtures.Builder(model, teamId, completenessThreshold, rules).build();
        title = String.format("Fixtures: %s", fixtures.getTeam().getName());
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
