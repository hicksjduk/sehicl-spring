package uk.org.sehicl.website.report;

import java.util.stream.Stream;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Player;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.data.TeamInMatch;

public class TeamSelector implements AveragesSelector
{
    private final String teamIdPattern;

    public TeamSelector(String teamIdPattern)
    {
        this.teamIdPattern = teamIdPattern;
    }

    @Override
    public boolean isSelected(League league)
    {
        return league.getTeams().stream().map(Team::getId).anyMatch(s -> s.matches(teamIdPattern));
    }

    @Override
    public boolean isSelected(Match match)
    {
        return Stream
                .of(match.getHomeTeamId(), match.getAwayTeamId())
                .anyMatch(s -> s.matches(teamIdPattern));
    }

    @Override
    public boolean isSelected(TeamInMatch teamInMatch)
    {
        return teamInMatch.getTeamId().matches(teamIdPattern);
    }

    @Override
    public String getUniqueId(Player player)
    {
        return player.getName();
    }
}
