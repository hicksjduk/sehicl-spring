package uk.org.sehicl.website.page;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import uk.org.sehicl.website.Constants;
import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.BattingAverages;
import uk.org.sehicl.website.report.BowlingAverages;
import uk.org.sehicl.website.report.TeamSelector;
import uk.org.sehicl.website.rules.Rules;

public class TeamAveragesPage extends Page
{
    private final BattingAverages batting;
    private final BowlingAverages bowling;
    private final String title;
    private final TeamSelector selector;
    private final Team team;
    private final boolean current;

    public TeamAveragesPage(String teamId, String uri)
    {
        super("averages", "teamaverages.ftlh", Section.AVERAGES, uri);
        this.selector = new TeamSelector(teamId);
        final Model model = ModelLoader.getModel();
        team = model.getTeam(teamId);
        final Rules rules = new Rules.Builder().build();
        final Completeness completenessThreshold = Completeness.CONSISTENT;
        batting = new BattingAverages.Builder(model, selector, completenessThreshold, rules, null)
                .build();
        bowling = new BowlingAverages.Builder(model, selector, completenessThreshold, rules, null)
                .build();
        title = String.format("Averages: %s", team.getName());
        current = true;
    }

    public TeamAveragesPage(String teamId, Integer season, String uri)
    {
        super("averages", "teamaverages.ftlh", Section.ARCHIVE, uri);
        this.selector = new TeamSelector(teamId);
        final Map<Integer, Model> modelsBySeason = IntStream
                .rangeClosed(season == null ? Constants.FIRST_SEASON : season,
                        season == null ? Constants.CURRENT_SEASON : season)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), ModelLoader::getModel))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().getLeagues().stream().anyMatch(selector::isSelected))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        final Map<Integer, Rules> rulesBySeason = modelsBySeason.keySet().stream().collect(
                Collectors.toMap(Function.identity(), s -> new Rules.Builder(s).build()));
        final Completeness completenessThreshold = Completeness.COMPLETE;
        batting = modelsBySeason
                .entrySet()
                .stream()
                .map(e -> new BattingAverages.Builder(e.getValue(), selector, completenessThreshold,
                        rulesBySeason.get(e.getKey()), null).build())
                .reduce(BattingAverages::merge)
                .orElse(null);
        bowling = modelsBySeason
                .entrySet()
                .stream()
                .map(e -> new BowlingAverages.Builder(e.getValue(), selector, completenessThreshold,
                        rulesBySeason.get(e.getKey()), null).build())
                .reduce(BowlingAverages::merge)
                .orElse(null);
        team = modelsBySeason.values().stream().findFirst().get().getTeam(teamId);
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
