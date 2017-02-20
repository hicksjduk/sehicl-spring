package uk.org.sehicl.website.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.sehicl.website.data.AwardedMatch;
import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.PlayedMatch;
import uk.org.sehicl.website.data.PointsDeduction;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.data.TeamInMatch;
import uk.org.sehicl.website.report.ReportStatus.Status;
import uk.org.sehicl.website.rules.Rules;

public class LeagueTable
{
    private static final Logger LOG = LoggerFactory.getLogger(LeagueTable.class);

    private final League league;
    private final Rules rules;
    private final Collection<TableRow> rows;
    private final ReportStatus status;
    private final List<String> deductionStrings;

    private LeagueTable(League league, Rules rules, Collection<TableRow> rows, ReportStatus status,
            List<String> deductionStrings)
    {
        this.league = league;
        this.rules = rules;
        this.rows = rows;
        this.status = status;
        this.deductionStrings = deductionStrings;
    }

    public Date getLastIncludedDate()
    {
        return status.getLastIncludedDate();
    }

    public int getToCome()
    {
        return status.getToCome();
    }

    public Status getStatus()
    {
        return status.getStatus();
    }

    public Collection<TableRow> getRows()
    {
        return rows;
    }

    public League getLeague()
    {
        return league;
    }

    public List<String> getDeductionStrings()
    {
        return deductionStrings;
    }

    public Rules getRules()
    {
        return rules;
    }

    public static class TableRow implements Comparable<TableRow>
    {
        private final Team team;
        private final Rules rules;
        private int won = 0;
        private int lost = 0;
        private int tied = 0;
        private int battingPoints = 0;
        private int bowlingPoints = 0;
        private int runsScored = 0;
        private int runRateBalls = 0;
        private final List<Integer> deductionKeys = new ArrayList<>();

        public TableRow(Team team, Rules rules)
        {
            this.team = team;
            this.rules = rules;
        }

        public int getPlayed()
        {
            return won + lost + tied;
        }

        public int getWon()
        {
            return won;
        }

        public int getLost()
        {
            return lost;
        }

        public int getTied()
        {
            return tied;
        }

        public int getBattingPoints()
        {
            return battingPoints;
        }

        public int getBowlingPoints()
        {
            return bowlingPoints;
        }

        public Team getTeam()
        {
            return team;
        }

        public int getPoints()
        {
            return IntStream
                    .of(won * rules.getPointsPerWin(), tied * rules.getPointsPerTie(),
                            lost * rules.getPointsPerDefeat(), battingPoints, bowlingPoints,
                            0 - getPointsDeducted())
                    .sum();
        }

        public Double getRunRate()
        {
            return runRateBalls == 0 ? null
                    : runsScored * rules.getBallsPerOver() * 1.0 / runRateBalls;
        }

        public int getPointsDeducted()
        {
            return team.getPointsDeductions().stream().mapToInt(PointsDeduction::getPoints).sum();
        }

        public List<String> getPointsDeductionStrings()
        {
            return team
                    .getPointsDeductions()
                    .stream()
                    .map(pd -> String.format("%s - %d %s deducted", pd.getReason(), pd.getPoints(),
                            pd.getPoints() == 1 ? "point" : "points"))
                    .collect(Collectors.toList());
        }

        @Override
        public int compareTo(TableRow o)
        {
            int answer = compareSortFields(o);
            return answer == 0 ? team.getName().compareTo(o.team.getName()) : answer;
        }

        public int compareSortFields(TableRow o)
        {
            int answer = o.getPoints() - getPoints();
            if (answer == 0)
            {
                answer = compareRunRates(o.getRunRate(), getRunRate());
            }
            return answer;
        }

        private int compareRunRates(Double rr1, Double rr2)
        {
            return rr1 == rr2 ? 0 : rr1 == null ? 1 : rr2 == null ? -1 : rr1.compareTo(rr2);
        }

        public void add(Match match)
        {
            add(match.getAwardedMatch());
            add(match.getPlayedMatch());
        }

        private void add(AwardedMatch awardedMatch)
        {
            if (awardedMatch != null)
            {
                if (Objects.equals(team.getId(), awardedMatch.getWinnerId()))
                {
                    won++;
                    battingPoints += rules.getBattingPointsForAwardedMatch();
                    bowlingPoints += rules.getBowlingPointsForAwardedMatch();
                }
                else
                {
                    lost++;
                }
            }
        }

        private void add(PlayedMatch playedMatch)
        {
            if (playedMatch != null)
            {
                final Iterator<TeamInMatch> iterator = playedMatch
                        .getTeams()
                        .stream()
                        .sorted((t1, t2) -> Boolean.compare(
                                Objects.equals(t2.getTeamId(), team.getId()),
                                Objects.equals(t1.getTeamId(), team.getId())))
                        .iterator();
                TeamInMatch thisTeam = iterator.next();
                TeamInMatch opponent = iterator.next();
                final int runsFor = thisTeam.getInnings().getRunsScored();
                final int runsAgainst = opponent.getInnings().getRunsScored();
                if (runsFor > runsAgainst)
                {
                    won++;
                }
                else if (runsFor < runsAgainst)
                {
                    lost++;
                }
                else
                {
                    tied++;
                }
                battingPoints += rules.getBattingPoints(runsFor,
                        (runsFor > runsAgainst && !thisTeam.isBattingFirst())
                                ? thisTeam.getInnings().getWicketsLost() : null);
                bowlingPoints += rules.getBowlingPoints(opponent.getInnings().getWicketsLost());
                runsScored += runsFor;
                runRateBalls += rules.getRunRateBalls(thisTeam.getInnings().getBallsBowled(),
                        thisTeam.getInnings().getWicketsLost(), playedMatch.getOverLimit());
            }
        }

        private void processDeductions(List<String> descriptions)
        {
            getPointsDeductionStrings()
                    .stream()
                    .map(str -> processDeductionDescription(str, descriptions))
                    .sorted()
                    .forEach(deductionKeys::add);
        }

        private int processDeductionDescription(String str, List<String> descriptions)
        {
            int answer = descriptions.indexOf(str);
            if (answer < 0)
            {
                descriptions.add(str);
                answer = descriptions.size();
            }
            return answer;
        }

        public List<Integer> getDeductionKeys()
        {
            return deductionKeys;
        }
    }

    public static class Builder
    {
        private final ReportStatus status = new ReportStatus();
        private final Map<String, TableRow> rowsByTeamId = new HashMap<>();
        private final League league;
        private final Rules rules;
        private final Completeness completenessThreshold;

        public Builder(League league, Rules rules, Completeness completenessThreshold)
        {
            this.league = league;
            this.rules = rules;
            this.completenessThreshold = completenessThreshold;
        }

        public LeagueTable build()
        {
            league.getTeams().stream().filter(t -> !t.isExcludedFromTables()).forEach(
                    t -> rowsByTeamId.put(t.getId(), new TableRow(t, rules)));
            league.getMatches().stream().forEach(this::add);
            Collection<TableRow> rows = new TreeSet<>(rowsByTeamId.values());
            List<String> deductionStrings = new ArrayList<>();
            rows.stream().forEach(r -> r.processDeductions(deductionStrings));
            return new LeagueTable(league, rules, rows, status, deductionStrings);
        }

        private void add(Match match)
        {
            final boolean complete = completenessThreshold
                    .compareTo(match.getCompleteness(rules)) <= 0;
            if (complete)
            {
                Stream
                        .of(match.getHomeTeamId(), match.getAwayTeamId())
                        .map(rowsByTeamId::get)
                        .filter(Objects::nonNull)
                        .forEach(r -> r.add(match));
            }
            else
            {
                if (match.getOutcome() != null)
                {
                    LOG.warn(match.toString());
                }
            }
            status.add(match, complete);
        }
    }
}
