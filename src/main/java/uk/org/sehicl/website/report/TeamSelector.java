package uk.org.sehicl.website.report;

import java.util.List;
import java.util.Objects;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Player;
import uk.org.sehicl.website.data.TeamInMatch;

public class TeamSelector implements AveragesSelector
{
    private final List<String> teamIds;

    public TeamSelector(String teamId)
    {
        this(List.of(teamId));
    }

    public TeamSelector(List<String> teamIds)
    {
        this.teamIds = teamIds;
    }

    @Override
    public boolean isSelected(League league)
    {
        return teamIds.stream().map(league::getTeam).anyMatch(Objects::nonNull);
    }

    @Override
    public boolean isSelected(Match match)
    {
        return teamIds
                .stream()
                .anyMatch(List.of(match.getHomeTeamId(), match.getAwayTeamId())::contains);
    }

    @Override
    public boolean isSelected(TeamInMatch teamInMatch, boolean batting)
    {
        return teamIds
                .stream()
                .anyMatch(teamId -> batting == Objects.equals(teamInMatch.getTeamId(), teamId));
    }

    @Override
    public String getUniqueId(Player player)
    {
        return player.getName();
    }
}
