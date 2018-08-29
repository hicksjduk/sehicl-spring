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

import uk.org.sehicl.website.data.Bowler;
import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Model;
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

        public void add(Bowler bowler)
        {
            balls += bowler.getBallsBowled();
            runs += bowler.getRunsConceded();
            wickets += bowler.getWicketsTaken();
            if (best == null || bowler.compareTo(best) < 0)
            {
                best = bowler;
            }
        }

        public int getWickets()
        {
            return wickets;
        }
    }

    public static class Builder
    {
        private final Model model;
        private final Map<String, BowlingRow> rowsByPlayerId = new HashMap<>();
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

        public BowlingAverages build()
        {
            model.getLeagues().stream().filter(selector::isSelected).forEach(this::add);
            return new BowlingAverages(this);
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
                        .filter(t -> selector.isSelected(t, false))
                        .forEach(t -> this.add(league, match, t));
            }
        }

        private void add(League league, Match match, TeamInMatch teamInMatch)
        {
            String teamId = Objects.equals(teamInMatch.getTeamId(), match.getHomeTeamId())
                    ? match.getAwayTeamId() : match.getHomeTeamId();
            final Team team = league.getTeam(teamId);
            teamInMatch.getInnings().getBowlers().stream().forEach(b -> this.add(team, b));
        }

        private void add(Team team, Bowler bowler)
        {
            final String playerId = bowler.getPlayerId();
            BowlingRow row = rowsByPlayerId.get(playerId);
            if (row == null)
            {
                row = new BowlingRow(team.getPlayer(playerId), team, rules);
                rowsByPlayerId.put(playerId, row);
            }
            row.add(bowler);
        }

        public Collection<BowlingRow> getRows()
        {
            SortedSet<BowlingRow> sortedRows = new TreeSet<>(rowsByPlayerId.values());
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
}
