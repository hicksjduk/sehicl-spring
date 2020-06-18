package uk.org.sehicl.website.resultentry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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

    private final String date;
    private final String time;
    private final String court;
    private final String leagueId;
    private final String leagueName;
    private final TeamData homeTeam;
    private final TeamData awayTeam;
    private List<InningsData> innings;

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
        homeTeam = getTeamData(league, homeTeamId, true);
        awayTeam = getTeamData(league, awayTeamId, false);
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

    public void enrich(DataExtractor extractor)
    {
        innings = IntStream
                .rangeClosed(1, 2)
                .mapToObj(sequence -> getInnings(extractor, sequence))
                .collect(Collectors.toList());
    }

    private InningsData getInnings(DataExtractor extractor, int innings)
    {
        InningsData answer = extractor.getInnings(innings);
        answer
                .setBatting(IntStream
                        .rangeClosed(1, 6)
                        .mapToObj(sequence -> extractor.getBatting(innings, sequence))
                        .collect(Collectors.toList()));
        answer
                .setBowling(IntStream
                        .rangeClosed(1, 6)
                        .mapToObj(sequence -> extractor.getBowling(innings, sequence))
                        .collect(Collectors.toList()));
        return answer;
    }

    private TeamData getTeamData(League l, String teamId, boolean isHome) throws ResultException
    {
        Team t = l.getTeam(teamId);
        if (t == null)
            throw ResultException.create("Team %s not found in league %s", teamId, leagueId);
        TeamData answer = new TeamData(t);
        return answer;
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

    public List<InningsData> getInningsData()
    {
        return innings;
    }

    public void setInnings(List<InningsData> innings)
    {
        this.innings = innings;
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

    @JsonInclude(Include.NON_NULL)
    public static class TeamData
    {
        public final String id;
        public final String name;
        public final List<PlayerData> players;
        public InningsData innings;

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

        public InningsData getInnings()
        {
            return innings;
        }

        public void setInnings(InningsData innings)
        {
            this.innings = innings;
        }
    }

    public static class InningsData
    {
        private final boolean homeTeamBatting;
        private final Integer extras;
        private final Integer total;
        private final Integer wickets;
        private final Integer balls;
        private List<BattingData> batting;
        private List<BowlingData> bowling;

        public InningsData(boolean homeTeamBatting, Integer extras, Integer total, Integer wickets, Integer balls)
        {
            this.homeTeamBatting = homeTeamBatting;
            this.extras = extras;
            this.total = total;
            this.wickets = wickets;
            this.balls = balls;
        }

        public boolean isHomeTeamBatting()
        {
            return homeTeamBatting;
        }

        public Integer getExtras()
        {
            return extras;
        }

        public Integer getTotal()
        {
            return total;
        }

        public Integer getWickets()
        {
            return wickets;
        }

        public Integer getBalls()
        {
            return balls;
        }

        public List<BattingData> getBatting()
        {
            return batting;
        }

        public void setBatting(List<BattingData> batting)
        {
            this.batting = batting;
        }

        public List<BowlingData> getBowling()
        {
            return bowling;
        }

        public void setBowling(List<BowlingData> bowling)
        {
            this.bowling = bowling;
        }
    }

    public static class PlayerData
    {
        private final String id;
        private final String name;

        public PlayerData(Player player)
        {
            this.id = player.getId();
            this.name = player.getName();
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

    public static class BattingData
    {
        private final String batsman;
        private final HowOut howOut;
        private final String bowler;
        private final Integer runs;

        public BattingData(String batsman, HowOut howOut, String bowler, Integer runs)
        {
            this.batsman = batsman;
            this.howOut = howOut;
            this.bowler = bowler;
            this.runs = runs;
        }

        public String getBatsman()
        {
            return batsman;
        }

        public HowOut getHowOut()
        {
            return howOut;
        }

        public String getBowler()
        {
            return bowler;
        }

        public Integer getRuns()
        {
            return runs;
        }
    }

    public static class BowlingData
    {
        private final String bowler;
        private final Integer balls;
        private final Integer runs;
        private final int wickets;

        public BowlingData(String bowler, Integer balls, Integer runs, Integer wickets)
        {
            this.bowler = bowler;
            this.balls = balls;
            this.runs = runs;
            this.wickets = wickets;
        }

        public String getBowler()
        {
            return bowler;
        }

        public Integer getBalls()
        {
            return balls;
        }

        public Integer getRuns()
        {
            return runs;
        }

        public Integer getWickets()
        {
            return wickets;
        }
    }
}
