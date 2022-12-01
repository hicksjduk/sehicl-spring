package uk.org.sehicl.website.report;

import java.util.Arrays;
import java.util.Objects;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Player;
import uk.org.sehicl.website.data.TeamInMatch;

public class TeamSelector implements AveragesSelector
{
    private final String teamId;
    
    public TeamSelector(String teamId)
    {
        this.teamId = teamId;
    }

    @Override
    public boolean isSelected(League league)
    {
        boolean answer = league.getTeam(teamId) != null;
        return answer;
    }

    @Override
    public boolean isSelected(Match match)
    {
        boolean answer = Arrays.asList(match.getHomeTeamId(), match.getAwayTeamId()).contains(teamId);
        return answer;
    }

    @Override
    public boolean isSelected(TeamInMatch teamInMatch, boolean batting)
    {
        boolean answer = batting == Objects.equals(teamInMatch.getTeamId(), teamId);
        return answer;
    }

    @Override
    public String getUniqueId(Player player)
    {
        return player.getName();
    }
}
