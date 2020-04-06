package uk.org.sehicl.website.report;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import uk.org.sehicl.website.data.Batsman;
import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
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
        private final SortedSet<BattingPerformance> performances = new TreeSet<>();

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

        public void add(BattingPerformance performance)
        {
            Batsman batsman = performance.performance;
            innings++;
            notOut += batsman.isOut() ? 0 : 1;
            runs += batsman.getRunsScored();
            if (best == null || batsman.compareTo(best) < 0)
            {
                best = batsman;
            }
            performances.add(performance);
        }

        public SortedSet<BattingPerformance> getPerformances()
        {
            return performances;
        }
    }

    public static class Builder
    {
        private final Map<String, BattingRow> rowsByUniqueId = new HashMap<>();
        private final AveragesSelector selector;
        private final ReportStatus status = new ReportStatus();
        private final Completeness completenessThreshold;
        private final Integer maxRows;
        private final ModelAndRules[] seasonData;

        public Builder(AveragesSelector selector, Completeness completenessThreshold,
                Integer maxRows, ModelAndRules... seasonData)
        {
            this.selector = selector;
            this.completenessThreshold = completenessThreshold;
            this.maxRows = maxRows;
            this.seasonData = seasonData;
        }

        public BattingAverages build()
        {
            Stream.of(seasonData).forEach(
                    sd -> sd.model.getLeagues().stream().filter(selector::isSelected).forEach(
                            l -> add(l, sd.rules)));
            return new BattingAverages(this);
        }

        private void add(League league, Rules rules)
        {
            league.getMatches().stream().filter(m -> selector.isSelected(m)).forEach(
                    m -> this.add(league, m, rules));
        }

        private void add(League league, Match match, Rules rules)
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
                        .forEach(t -> this.add(league, t, match.getDateTime(),
                                league.getTeam(match.getOpponentId(t.getTeamId()))));
            }
        }

        private void add(League league, TeamInMatch teamInMatch, Date matchDate, Team opponent)
        {
            final Team team = league.getTeam(teamInMatch.getTeamId());
            teamInMatch.getInnings().getBatsmen().stream().forEach(
                    b -> this.add(team, b, matchDate, opponent));
        }

        private void add(Team team, Batsman batsman, Date matchDate, Team opponent)
        {
            final Player player = team.getPlayer(batsman.getPlayerId());
            final String uniqueId = selector.getUniqueId(player);
            BattingRow row = rowsByUniqueId.get(uniqueId);
            if (row == null)
            {
                row = new BattingRow(player, team);
                rowsByUniqueId.put(uniqueId, row);
            }
            row.add(new BattingPerformance(matchDate, opponent, batsman));
        }

        public Collection<BattingRow> getRows()
        {
            SortedSet<BattingRow> sortedRows = new TreeSet<>(rowsByUniqueId.values());
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

    public static class BattingPerformance implements Comparable<BattingPerformance>
    {
        private final static Comparator<BattingPerformance> COMPARATOR = Comparator
                .comparing(bp -> bp.matchDate);

        public final Date matchDate;
        public final Team opponent;
        public final Batsman performance;

        public BattingPerformance(Date matchDate, Team opponent, Batsman performance)
        {
            this.matchDate = matchDate;
            this.opponent = opponent;
            this.performance = performance;
        }

        @Override
        public int compareTo(BattingPerformance other)
        {
            return COMPARATOR.compare(this, other);
        }

        public Date getMatchDate()
        {
            return matchDate;
        }

        public Team getOpponent()
        {
            return opponent;
        }

        public Batsman getPerformance()
        {
            return performance;
        }
    }
}
