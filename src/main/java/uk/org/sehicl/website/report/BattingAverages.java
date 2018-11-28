package uk.org.sehicl.website.report;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uk.org.sehicl.website.data.Batsman;
import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.data.Player;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.data.TeamInMatch;
import uk.org.sehicl.website.report.BattingAverages.BattingRow;
import uk.org.sehicl.website.report.ReportStatus.Status;
import uk.org.sehicl.website.rules.Rules;

public class BattingAverages implements Averages<BattingRow>
{
    private final Collection<BattingRow> rows;
    private final ReportStatus status;

    private BattingAverages(Builder builder)
    {
        rows = builder.getRows();
        status = builder.status;
    }

    @Override
    public Collection<BattingRow> getRows()
    {
        return rows;
    }

    @Override
    public Date getLastIncludedDate()
    {
        return status.getLastIncludedDate();
    }

    @Override
    public int getToCome()
    {
        return status.getToCome();
    }

    @Override
    public Status getStatus()
    {
        return status.getStatus();
    }

    public static class BattingRow implements Comparable<BattingRow>
    {
        private static final Comparator<BattingRow> SK_COMPARATOR = Comparator
                .comparingInt(BattingRow::getRuns)
                .reversed();
        private static final Comparator<BattingRow> COMPARATOR = SK_COMPARATOR
                .thenComparing(BattingRow::getPlayer);

        private final Player player;
        private final Team team;
        private int innings;
        private int notOut;
        private int runs;
        private Batsman best;

        public BattingRow(Player player, Team team)
        {
            this.player = player;
            this.team = team;
        }

        public Player getPlayer()
        {
            return player;
        }

        public Team getTeam()
        {
            return team;
        }

        public int getInnings()
        {
            return innings;
        }

        public int getNotOut()
        {
            return notOut;
        }

        public int getRuns()
        {
            return runs;
        }

        public Batsman getBest()
        {
            return best;
        }

        public Double getAverage()
        {
            int dismissals = innings - notOut;
            return dismissals == 0 ? null : runs * 1.0 / dismissals;
        }

        @Override
        public int compareTo(BattingRow o)
        {
            return COMPARATOR.compare(this, o);
        }

        public int compareSortKeys(BattingRow o)
        {
            return SK_COMPARATOR.compare(this, o);
        }

        public void add(Batsman batsman)
        {
            innings++;
            notOut += batsman.isOut() ? 0 : 1;
            runs += batsman.getRunsScored();
            if (best == null || batsman.compareTo(best) < 0)
            {
                best = batsman;
            }
        }
    }

    public static class Builder
    {
        private final Model model;
        private final Map<String, BattingRow> rowsByPlayerId = new HashMap<>();
        private final AveragesSelector selector;
        private final ReportStatus status = new ReportStatus();
        private final Completeness completenessThreshold;
        private final Rules rules;
        private final Integer maxRows;

        public Builder(Model model, AveragesSelector selector, Completeness completenessThreshold,
                Rules rules, Integer maxRows)
        {
            this.model = model;
            this.selector = selector;
            this.completenessThreshold = completenessThreshold;
            this.rules = rules;
            this.maxRows = maxRows;
        }

        public BattingAverages build()
        {
            model.getLeagues().stream().filter(selector::isSelected).forEach(this::add);
            return new BattingAverages(this);
        }

        private void add(League league)
        {
            league.getMatches().stream().filter(m -> selector.isSelected(m)).forEach(
                    m -> this.add(league, m));
        }

        private void add(League league, Match match)
        {
            boolean complete = completenessThreshold.compareTo(match.getCompleteness(rules)) <= 0;
            status.add(match, complete);
            if (complete && match.getPlayedMatch() != null)
            {
                match
                        .getPlayedMatch()
                        .getTeams()
                        .stream()
                        .filter(t -> selector.isSelected(t, true))
                        .forEach(t -> this.add(league, t));
            }
        }

        private void add(League league, TeamInMatch teamInMatch)
        {
            final Team team = league.getTeam(teamInMatch.getTeamId());
            teamInMatch.getInnings().getBatsmen().stream().forEach(b -> this.add(team, b));
        }

        private void add(Team team, Batsman batsman)
        {
            final String playerId = batsman.getPlayerId();
            BattingRow row = rowsByPlayerId.get(playerId);
            if (row == null)
            {
                row = new BattingRow(team.getPlayer(playerId), team);
                rowsByPlayerId.put(playerId, row);
            }
            row.add(batsman);
        }

        public Collection<BattingRow> getRows()
        {
            SortedSet<BattingRow> sortedRows = new TreeSet<>(rowsByPlayerId.values());
            Collection<BattingRow> answer;
            if (maxRows == null || maxRows >= sortedRows.size())
            {
                answer = sortedRows;
            }
            else
            {
                answer = new LinkedList<>();
                BattingRow lastRow = null;
                int count = 0;
                for (BattingRow row : sortedRows)
                {
                    if (++count > maxRows && row.compareSortKeys(lastRow) != 0)
                    {
                        break;
                    }
                    answer.add(row);
                    lastRow = row;
                }
            }
            return answer;
        }
    }

    public BattingAverages merge(BattingAverages other)
    {
        Map<String, BattingRow> rowsByName = rows.stream().collect(
                Collectors.toMap(row -> row.getPlayer().getName(), Function.identity()));
        other.rows.stream().forEach(otherRow ->
        {
            String name = otherRow.getPlayer().getName();
            BattingRow mergeRow = rowsByName.get(name);
            if (mergeRow == null)
                rowsByName.put(name, otherRow);
            else
            {
                mergeRow.innings += otherRow.innings;
                mergeRow.notOut += otherRow.notOut;
                mergeRow.runs += otherRow.runs;
                mergeRow.best = Stream
                        .of(mergeRow.best, otherRow.best)
                        .filter(Objects::nonNull)
                        .sorted()
                        .findFirst()
                        .orElse(null);
            }
        });
        rows.clear();
        rows.addAll(new TreeSet<>(rowsByName.values()));
        return this;
    }
}
