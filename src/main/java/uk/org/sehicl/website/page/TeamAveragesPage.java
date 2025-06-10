package uk.org.sehicl.website.page;

import java.util.stream.IntStream;

import uk.org.sehicl.website.Constants;
import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.BattingAverages;
import uk.org.sehicl.website.report.BowlingAverages;
import uk.org.sehicl.website.report.ModelAndRules;
import uk.org.sehicl.website.report.TeamSelector;

public class TeamAveragesPage extends Page
{
    private final BattingAverages batting;
    private final BowlingAverages bowling;
    private final String title;
    private final TeamSelector selector;
    private final Team team;
    private final boolean current;

    public TeamAveragesPage(String teamId)
    {
        super("averages", "teamaverages.ftlh", Section.AVERAGES);
        this.selector = new TeamSelector(teamId);
        final Completeness completenessThreshold = Completeness.CONSISTENT;
        final ModelAndRules modelAndRules = new ModelAndRules();
        team = modelAndRules.model.getTeam(teamId);
        batting = new BattingAverages.Builder(selector, completenessThreshold, null, modelAndRules)
                .build();
        bowling = new BowlingAverages.Builder(selector, completenessThreshold, null, modelAndRules)
                .build();
        title = String.format("Averages: %s", team.getName());
        current = true;
    }

    public TeamAveragesPage(String teamId, Integer season)
    {
        super("averages", "teamaverages.ftlh", Section.ARCHIVE);
        this.selector = new TeamSelector(teamId);
        final ModelAndRules[] seasonData = IntStream
                .rangeClosed(season == null ? Constants.FIRST_SEASON : season,
                        season == null ? Constants.CURRENT_SEASON : season)
                .filter(i -> i != 21)
                .mapToObj(ModelAndRules::new)
                .filter(sd -> sd.model.getLeagues().stream().anyMatch(selector::isSelected))
                .toArray(ModelAndRules[]::new);
        final Completeness completenessThreshold = Completeness.COMPLETE;
        batting = new BattingAverages.Builder(selector, completenessThreshold, null, seasonData)
                .build();
        bowling = new BowlingAverages.Builder(selector, completenessThreshold, null, seasonData)
                .build();
        team = seasonData[0].model.getTeam(teamId);
        title = String.format("Averages: %s", team.getName());
        current = false;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    public BattingAverages getBatting()
    {
        return batting;
    }

    public BowlingAverages getBowling()
    {
        return bowling;
    }

    public Team getTeam()
    {
        return team;
    }

    public boolean isCurrent()
    {
        return current;
    }
}
