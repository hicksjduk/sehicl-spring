package uk.org.sehicl.website.resultentry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import uk.org.sehicl.website.OrdinalDateFormatter;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.data.Player;
import uk.org.sehicl.website.data.Team;

@JsonPropertyOrder({ "date", "time", "court", "leagueId", "leagueName", "homeTeam", "awayTeam" })
public class MatchData
{
    private final static OrdinalDateFormatter DATE_FORMAT = new OrdinalDateFormatter(
            new SimpleDateFormat("d MMMM yyyy"));
    private final static DateFormat TIME_FORMAT = new SimpleDateFormat("h:mm");

    public final String date;
    public final String time;
    public final String court;
    public final String leagueId;
    public final String leagueName;
    public final TeamData homeTeam;
    public final TeamData awayTeam;

    public MatchData(Model model, String leagueId, String homeTeamId, String awayTeamId)
            throws ResultException
    {
        final League league = model.getLeague(leagueId);
        if (league == null)
            throw ResultException.create("League %s not found", leagueId);
        this.leagueId = league.getId();
        leagueName = league.getName();
        Team ht = league.getTeam(homeTeamId);
        if (ht == null)
            throw ResultException.create("Team %s not found in league %s", homeTeamId, leagueId);
        homeTeam = getTeamData(league, homeTeamId);
        awayTeam = getTeamData(league, awayTeamId);
        Match match = league
                .getMatches()
                .stream()
                .filter(m -> m.getHomeTeamId().equals(homeTeamId)
                        && m.getAwayTeamId().equals(awayTeamId))
                .findFirst()
                .orElse(null);
        if (match == null)
            throw ResultException
                    .create("Match between %s and %s not found in league %s", homeTeamId,
                            awayTeamId, leagueId);
        date = DATE_FORMAT.format(match.getDateTime());
        time = TIME_FORMAT.format(match.getDateTime());
        court = match.getCourt();
    }

    public MatchData(Model model, String leagueId, String homeTeamId, String awayTeamId,
            UnaryOperator<String> fieldExtractor) throws ResultException
    {
        this(model, leagueId, homeTeamId, awayTeamId);
        
    }

    private TeamData getTeamData(League l, String teamId) throws ResultException
    {
        Team t = l.getTeam(teamId);
        if (t == null)
            throw ResultException.create("Team %s not found in league %s", teamId, leagueId);
        return new TeamData(t);
    }

    public TeamData getHomeTeam()
    {
        return homeTeam;
    }

    public String getDate()
    {
        return date;
    }

    public String getTime()
    {
        return time;
    }

    public String getCourt()
    {
        return court;
    }

    public String getLeagueId()
    {
        return leagueId;
    }

    public String getLeagueName()
    {
        return leagueName;
    }

    public TeamData getAwayTeam()
    {
        return awayTeam;
    }
    
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

    public static class TeamData
    {
        public final String id;
        public final String name;
        public final List<PlayerData> players;

        public TeamData(Team t)
        {
            this.id = t.getId();
            this.name = t.getName();
            this.players = t
                    .getPlayers()
                    .stream()
                    .sorted()
                    .map(PlayerData::new)
                    .collect(Collectors.toList());
        }

        public String getName()
        {
            return name;
        }

        public String getId()
        {
            return id;
        }

        public List<PlayerData> getPlayers()
        {
            return players;
        }
    }

    public static class PlayerData
    {
        public final String id;
        public final String name;

        public PlayerData(Player p)
        {
            this.id = p.getId();
            this.name = p.getName();
        }

        public String getId()
        {
            return id;
        }

        public String getName()
        {
            return name;
        }
    }
}
