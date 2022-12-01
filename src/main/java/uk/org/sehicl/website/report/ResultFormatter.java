package uk.org.sehicl.website.report;

import java.util.Arrays;
import java.util.Comparator;

import uk.org.sehicl.website.data.AwardedMatch;
import uk.org.sehicl.website.data.Innings;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.PlayedMatch;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.data.TeamInMatch;
import uk.org.sehicl.website.rules.Rules;

public class ResultFormatter
{
    public static String format(League league, Match match, Rules rules)
    {
        return format(league, match, rules, null);
    }

    public static String format(League league, Match match, Rules rules, String teamId)
    {
        String answer = null;
        Margin margin = Margin.getMargin(league, match, rules);
        if (margin != null)
        {
            if (margin instanceof TiedMargin)
            {
                answer = teamId == null ? "Match tied" : "Tied";
            }
            else
            {
                Team winner = margin.getWinner();
                if (teamId == null)
                {
                    if (margin instanceof AwardMargin)
                    {
                        answer = String.format("by %s", margin);
                    }
                    else
                    {
                        answer = String.format("%s won by %s", winner.getName(), margin);
                    }
                }
                else
                {
                    answer = String.format("%s by %s",
                            teamId.equals(winner.getId()) ? "Won" : "Lost", margin);
                }
            }
        }
        else if (match.getUnplayedMatch() != null)
        {
            answer = String.format("Not played (%s)", match.getUnplayedMatch().getReason());
        }
        return answer;
    }

    private static abstract class Margin
    {
        private static final Comparator<TeamInMatch> TEAM_IN_MATCH_COMPARATOR = Comparator
                .comparing(TeamInMatch::getInnings,
                        Comparator.comparingInt(Innings::getRunsScored).reversed());

        private final Team winner;

        public Margin(Team winner)
        {
            this.winner = winner;
        }

        public static Margin getMargin(League league, Match match, Rules rules)
        {
            Margin answer = null;
            if (match.getOutcome() instanceof AwardedMatch)
            {
                String winnerId = match.getAwardedMatch().getWinnerId();
                Team winner = league.getTeam(winnerId);
                answer = new AwardMargin(winner, match.getAwardedMatch().getReason());
            }
            else if (match.getOutcome() instanceof PlayedMatch)
            {
                PlayedMatch playedMatch = match.getPlayedMatch();
                TeamInMatch[] teams = playedMatch.getTeams().toArray(new TeamInMatch[2]);
                Arrays.sort(teams, TEAM_IN_MATCH_COMPARATOR);
                if (TEAM_IN_MATCH_COMPARATOR.compare(teams[0], teams[1]) == 0)
                {
                    answer = new TiedMargin();
                }
                else
                {
                    Team winner = league.getTeam(teams[0].getTeamId());
                    if (teams[0].isBattingFirst())
                    {
                        int difference = teams[0].getInnings().getRunsScored()
                                - teams[1].getInnings().getRunsScored();
                        answer = new RunsMargin(winner, difference);
                    }
                    else
                    {
                        int wicketsInHand = rules.getMaxWickets()
                                - teams[0].getInnings().getWicketsLost();
                        answer = new WicketsMargin(winner, wicketsInHand);
                    }
                }
            }
            return answer;
        }

        public Team getWinner()
        {
            return winner;
        }
    }

    private static class TiedMargin extends Margin
    {
        public TiedMargin()
        {
            super(null);
        }
    }

    private static class RunsMargin extends Margin
    {
        private final int difference;

        public RunsMargin(Team winner, int difference)
        {
            super(winner);
            this.difference = difference;
        }

        @Override
        public String toString()
        {
            String answer = String.format("%d run%s", difference, difference == 1 ? "" : "s");
            return answer;
        }
    }

    private static class WicketsMargin extends Margin
    {
        private final int wicketsInHand;

        public WicketsMargin(Team winner, int wicketsInHand)
        {
            super(winner);
            this.wicketsInHand = wicketsInHand;
        }

        @Override
        public String toString()
        {
            String answer = String.format("%d wicket%s", wicketsInHand,
                    wicketsInHand == 1 ? "" : "s");
            return answer;
        }
    }

    private static class AwardMargin extends Margin
    {
        private final String reason;

        public AwardMargin(Team winner, String reason)
        {
            super(winner);
            this.reason = reason;
        }

        @Override
        public String toString()
        {
            String answer = String.format("default (%s)", reason);
            return answer;
        }
    }
}
