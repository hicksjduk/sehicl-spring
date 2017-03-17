package uk.org.sehicl.website.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;

import uk.org.sehicl.website.data.AwardedMatch;
import uk.org.sehicl.website.data.Innings;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Performance;
import uk.org.sehicl.website.data.PlayedMatch;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.data.TeamInMatch;
import uk.org.sehicl.website.rules.Rules;

public abstract class Results
{
    private final Collection<ResultDetails> results;

    protected Results(Collection<ResultDetails> results)
    {
        this.results = new TreeSet<>(results);
    }

    public Collection<ResultDetails> getResults()
    {
        return results;
    }

    public static abstract class ResultDetails implements Comparable<ResultDetails>
    {
        private final League league;
        private final Match match;

        public ResultDetails(League league, Match match)
        {
            this.league = league;
            this.match = match;
        }

        @Override
        public int compareTo(ResultDetails o)
        {
            int answer = league.compareTo(o.league);
            if (answer == 0)
            {
                answer = compareDates(match.getDateTime(), o.match.getDateTime());
                if (answer == 0)
                {
                    answer = match.getCourt().compareTo(o.match.getCourt());
                }
            }
            return answer;
        }

        private int compareDates(Date d1, Date d2)
        {
            int answer = DateUtils.truncatedCompareTo(d2, d1, Calendar.DATE);
            if (answer == 0)
            {
                answer = d1.compareTo(d2);
            }
            return answer;
        }

        public League getLeague()
        {
            return league;
        }

        public Match getMatch()
        {
            return match;
        }
    }

    public static class PlayedResultDetails extends ResultDetails
    {
        private final InningsDetails firstInnings;
        private final InningsDetails secondInnings;
        private final int maxOvers;
        private final int maxWickets;

        public PlayedResultDetails(League league, Match match, Rules rules)
        {
            super(league, match);
            final PlayedMatch playedMatch = match
                    .getPlayedMatch();
            final List<TeamInMatch> teams = playedMatch
                    .getTeams()
                    .stream()
                    .sorted((a, b) -> Boolean.compare(b.isBattingFirst(), a.isBattingFirst()))
                    .collect(Collectors.toList());
            Team teamBattingFirst = league.getTeam(teams.get(0).getTeamId());
            Team teamBattingSecond = league.getTeam(teams.get(1).getTeamId());
            firstInnings = new InningsDetails(true, teams.get(0).getInnings(), teamBattingFirst, teamBattingSecond, rules, playedMatch);
            secondInnings = new InningsDetails(false, teams.get(1).getInnings(), teamBattingSecond, teamBattingFirst, rules, playedMatch);
            maxOvers = playedMatch.getOverLimit() == null ? rules.getOversPerInnings()
                    : playedMatch.getOverLimit();
            maxWickets = rules.getMaxWickets();
        }

        public InningsDetails getFirstInnings()
        {
            return firstInnings;
        }

        public InningsDetails getSecondInnings()
        {
            return secondInnings;
        }

        public int getMaxOvers()
        {
            return maxOvers;
        }

        public int getMaxWickets()
        {
            return maxWickets;
        }
    }

    public static class InningsDetails implements Comparable<InningsDetails>
    {
        private final boolean first;
        private final Innings innings;
        private final Team battingTeam;
        private final Team bowlingTeam;
        private final Rules rules;
        private final Integer overLimit;

        public InningsDetails(boolean first, Innings innings, Team battingTeam, Team bowlingTeam,
                Rules rules, PlayedMatch playedMatch)
        {
            this.first = first;
            this.innings = innings;
            this.battingTeam = battingTeam;
            this.bowlingTeam = bowlingTeam;
            this.rules = rules;
            this.overLimit = playedMatch.getOverLimit();
        }

        public Team getTeam()
        {
            return battingTeam;
        }

        public int getRuns()
        {
            return innings.getRunsScored();
        }

        public int getWickets()
        {
            return innings.getWicketsLost(rules.getMaxWickets());
        }

        public double getOvers()
        {
            final int ballsPerOver = rules.getBallsPerOver();
            int maxBalls = (overLimit == null ? rules.getOversPerInnings() : overLimit)
                    * ballsPerOver;
            int balls = innings.getBallsBowled(maxBalls);
            double answer = (balls / ballsPerOver) + (0.1 * (balls % ballsPerOver));
            return answer;
        }

        public List<String> getHighlights()
        {
            List<String> answer = new ArrayList<>();
            innings
                    .getBatsmen()
                    .stream()
                    .filter(b -> b.getRunsScored() >= rules.getMinRunsForBattingHighlight())
                    .sorted((a, b) -> compare(a, b, battingTeam))
                    .map(b -> String.format("%s %d%s",
                            battingTeam.getPlayer(b.getPlayerId()).getName(), b.getRunsScored(),
                            b.isOut() ? "" : "*"))
                    .forEach(answer::add);
            innings
                    .getBowlers()
                    .stream()
                    .filter(b -> b.getWicketsTaken() >= rules.getMinWicketsForBowlingHighlight())
                    .sorted((a, b) -> compare(a, b, bowlingTeam))
                    .map(b -> String.format("%s %d/%d",
                            bowlingTeam.getPlayer(b.getPlayerId()).getName(), b.getWicketsTaken(),
                            b.getRunsConceded()))
                    .forEach(answer::add);
            return answer;
        }

        @Override
        public int compareTo(InningsDetails o)
        {
            return Boolean.compare(o.first, first);
        }
        
        private <T extends Performance & Comparable<T>> int compare(T a, T b, Team t)
        {
            int answer = a.compareTo(b);
            if (answer == 0)
            {
                answer = t.getPlayer(a.getPlayerId()).compareTo(t.getPlayer(b.getPlayerId()));
            }
            return answer;
        }
}

    public static class AwardedMatchDetails extends ResultDetails
    {
        private final String winnerId;
        private final String winnerName;
        private final String loserId;
        private final String loserName;
        private final String reason;

        public AwardedMatchDetails(League league, Match match)
        {
            super(league, match);
            final AwardedMatch awardedMatch = match.getAwardedMatch();
            winnerId = awardedMatch.getWinnerId();
            winnerName = league.getTeam(winnerId).getName();
            loserId = match.getHomeTeamId().equals(winnerId) ? match.getAwayTeamId()
                    : match.getHomeTeamId();
            loserName = league.getTeam(loserId).getName();
            reason = awardedMatch.getReason();
        }

        public String getWinnerId()
        {
            return winnerId;
        }

        public String getWinnerName()
        {
            return winnerName;
        }

        public String getLoserId()
        {
            return loserId;
        }

        public String getLoserName()
        {
            return loserName;
        }

        public String getReason()
        {
            return reason;
        }
    }
}
