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

import uk.org.sehicl.website.data.Bowler;
import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Player;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.data.TeamInMatch;
import uk.org.sehicl.website.report.BowlingAverages.BowlingRow;
import uk.org.sehicl.website.report.ReportStatus.Status;
import uk.org.sehicl.website.rules.Rules;

public class BowlingAverages implements Averages<BowlingRow>
{
    private final Collection<BowlingRow> rows;
    private final ReportStatus status;

    private BowlingAverages(Builder builder)
    {
        rows = builder.getRows();
        status = builder.status;
    }

    @Override
    public Collection<BowlingRow> getRows()
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

    public static class BowlingRow implements Comparable<BowlingRow>
    {
        private final static Comparator<BowlingRow> SK_COMPARATOR = Comparator
                .comparingInt(BowlingRow::getWickets)
                .reversed()
                .thenComparing(BowlingRow::getEconomyRate,
                        Comparator.nullsLast(Comparator.naturalOrder()));
        private final static Comparator<BowlingRow> COMPARATOR = SK_COMPARATOR
                .thenComparing(BowlingRow::getPlayer);

        private final Player player;
        private final Team team;
        private final Rules rules;
        private int balls;
        private int runs;
        private int wickets;
        private Bowler best;
        private final SortedSet<BowlingPerformance> performances = new TreeSet<>();

        public BowlingRow(Player player, Team team, Rules rules)
        {
            this.player = player;
            this.team = team;
            this.rules = rules;
        }

        public Player getPlayer()
        {
            return player;
        }

        public Team getTeam()
        {
            return team;
        }

        public int getBalls()
        {
            return balls;
        }

        public String getOvers()
        {
            return rules.ballsToOvers(balls);
        }

        public int getRuns()
        {
            return runs;
        }

        public Bowler getBest()
        {
            return best;
        }

        public Double getAveragePerWicket()
        {
            return wickets == 0 ? null : runs * 1.0 / wickets;
        }

        public Double getEconomyRate()
        {
            return balls == 0 ? null : runs * 1.0 * rules.getBallsPerOver() / balls;
        }

        @Override
        public int compareTo(BowlingRow o)
        {
            return COMPARATOR.compare(this, o);
        }

        public int compareSortKeys(BowlingRow o)
        {
            return SK_COMPARATOR.compare(this, o);
        }

        public void add(BowlingPerformance performance)
        {
            final Bowler bowler = performance.performance;
            balls += bowler.getBallsBowled();
            runs += bowler.getRunsConceded();
            wickets += bowler.getWicketsTaken();
            if (best == null || bowler.compareTo(best) < 0)
            {
                best = bowler;
            }
            performances.add(performance);
        }

        public int getWickets()
        {
            return wickets;
        }

        public SortedSet<BowlingPerformance> getPerformances()
        {
            return performances;
        }
    }

    public static class Builder
    {
        private final Map<String, BowlingRow> rowsByUniqueId = new HashMap<>();
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

        public BowlingAverages build()
        {
            Stream
                    .of(seasonData)
                    .forEach(sd -> sd.model
                            .getLeagues()
                            .stream()
                            .filter(selector::isSelected)
                            .forEach(l -> this.add(l, sd.rules)));
            return new BowlingAverages(this);
        }

        private void add(League league, Rules rules)
        {
            league
                    .getMatches()
                    .stream()
                    .filter(m -> selector.isSelected(m))
                    .forEach(m -> this.add(league, m, rules));
        }

        private void add(League league, Match match, Rules rules)
        {
            boolean complete = completenessThreshold.compareTo(match.getCompleteness(rules)) <= 0;
            status.add(match, complete);
            if (complete && match.getPlayedMatch() != null)
            {
                var teams = match.getPlayedMatch().getTeams();
                var bothTeamsSelected = teams.stream().allMatch(selector::isSelected);
                teams
                        .stream()
                        .filter(t -> bothTeamsSelected || !selector.isSelected(t))
                        .forEach(t -> this.add(league, match, t, rules));
            }
        }

        private void add(League league, Match match, TeamInMatch teamInMatch, Rules rules)
        {
            String teamId = match.getOpponentId(teamInMatch.getTeamId());
            Team opponent = league.getTeam(teamInMatch.getTeamId());
            final Team team = league.getTeam(teamId);
            teamInMatch
                    .getInnings()
                    .getBowlers()
                    .stream()
                    .forEach(b -> this.add(team, b, rules, match.getDateTime(), opponent));
        }

        private void add(Team team, Bowler bowler, Rules rules, Date matchDate, Team opponent)
        {
            final Player player = team.getPlayer(bowler.getPlayerId());
            final String uniqueId = selector.getUniqueId(player);
            BowlingRow row = rowsByUniqueId.get(uniqueId);
            if (row == null)
            {
                row = new BowlingRow(player, team, rules);
                rowsByUniqueId.put(uniqueId, row);
            }
            row.add(new BowlingPerformance(matchDate, opponent, bowler, rules));
        }

        public Collection<BowlingRow> getRows()
        {
            SortedSet<BowlingRow> sortedRows = new TreeSet<>(rowsByUniqueId.values());
            Collection<BowlingRow> answer;
            if (maxRows == null || maxRows >= sortedRows.size())
            {
                answer = sortedRows;
            }
            else
            {
                answer = new LinkedList<>();
                BowlingRow lastRow = null;
                int count = 0;
                for (BowlingRow row : sortedRows)
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

    public static class BowlingPerformance implements Comparable<BowlingPerformance>
    {
        private final static Comparator<BowlingPerformance> COMPARATOR = Comparator
                .comparing(bp -> bp.matchDate);

        public final Date matchDate;
        public final Team opponent;
        public final Bowler performance;
        public final String overs;

        public BowlingPerformance(Date matchDate, Team opponent, Bowler performance, Rules rules)
        {
            this.matchDate = matchDate;
            this.opponent = opponent;
            this.performance = performance;
            this.overs = rules.ballsToOvers(performance.getBallsBowled());
        }

        @Override
        public int compareTo(BowlingPerformance other)
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

        public Bowler getPerformance()
        {
            return performance;
        }

        public String getOvers()
        {
            return overs;
        }

    }
}
