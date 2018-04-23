package uk.org.sehicl.website.resultentry;

import java.util.Arrays;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.data.Team;

public class Result
{
    @SuppressWarnings("serial")
    public static class ResultException extends Exception
    {
        public static ResultException create(String message, Object... args)
        {
            int lastIndex = args.length - 1;
            boolean lastArgIsThrowable = lastIndex >= 0 && args[lastIndex] instanceof Throwable;
            return lastArgIsThrowable
                    ? new ResultException(
                            String.format(message, Arrays.copyOfRange(args, 0, lastIndex)),
                            (Throwable) args[lastIndex])
                    : new ResultException(String.format(message, args));
        }

        private ResultException(String arg0, Throwable arg1)
        {
            super(arg0, arg1);
        }

        private ResultException(String arg0)
        {
            super(arg0);
        }

        private ResultException(Throwable arg0)
        {
            super(arg0);
        }
    }

    private final Match match;
    private final Team homeTeam;
    private final Team awayTeam;

    public Match getMatch()
    {
        return match;
    }

    public Team getHomeTeam()
    {
        return homeTeam;
    }

    public Team getAwayTeam()
    {
        return awayTeam;
    }

    public Result(Model model, String leagueId, String homeTeamId, String awayTeamId)
            throws ResultException
    {
        final League league = model.getLeague(leagueId);
        if (league == null)
            throw ResultException.create("League %s not found", leagueId);
        homeTeam = league.getTeam(homeTeamId);
        if (homeTeam == null)
            throw ResultException.create("Team %s not found in league %s", homeTeamId, leagueId);
        awayTeam = league.getTeam(awayTeamId);
        if (awayTeam == null)
            throw ResultException.create("Team %s not found in league %s", awayTeamId, leagueId);
        match = league
                .getMatches()
                .stream()
                .filter(m -> m.getHomeTeamId().equals(homeTeamId)
                        && m.getAwayTeamId().equals(awayTeamId))
                .findFirst()
                .orElse(null);
        if (match == null)
            throw ResultException.create("Match between %s and %s not found in league %s",
                    homeTeamId, awayTeamId, leagueId);
    }
}
