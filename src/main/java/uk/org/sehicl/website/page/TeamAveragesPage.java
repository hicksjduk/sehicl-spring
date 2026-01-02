package uk.org.sehicl.website.page;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        this(List.of(teamId), season);
    }

    public TeamAveragesPage(List<String> teamIds, Integer season)
    {
        super("averages", "teamaverages.ftlh", Section.ARCHIVE);
        this.selector = new TeamSelector(teamIds);
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
        team = Stream
                .of(seasonData)
                .map(sd -> sd.model)
                .map(m -> m.getTeam(teamIds.get(0)))
                .filter(Objects::nonNull)
                .findFirst()
                .get();
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
