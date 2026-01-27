package uk.org.sehicl.website.report;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Player;
import uk.org.sehicl.website.data.TeamInMatch;

public interface AveragesSelector
{
    boolean isSelected(League league);
    
    boolean isSelected(Match match);
    
    boolean isSelected(TeamInMatch teamInMatch);
    
    String getUniqueId(Player player);
}
