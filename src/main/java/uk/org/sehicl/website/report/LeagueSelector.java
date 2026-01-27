package uk.org.sehicl.website.report;

import java.util.regex.Pattern;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Player;
import uk.org.sehicl.website.data.TeamInMatch;

public enum LeagueSelector implements AveragesSelector
{
    SENIOR("Division\\d", "Senior"), COLTSUNDER16("ColtsUnder16", "Colts Under-16"), COLTSUNDER13("ColtsUnder13", "Colts Under-13");
    
    private final Pattern leagueIdPattern;
    private final String name;
    
    private LeagueSelector(String regex, String name)
    {
        leagueIdPattern = Pattern.compile(regex);
        this.name = name;
    }

    @Override
    public boolean isSelected(League league)
    {
        boolean answer = leagueIdPattern.matcher(league.getId()).matches();
        return answer;
    }

    @Override
    public boolean isSelected(Match match)
    {
        return true;
    }

    @Override
    public boolean isSelected(TeamInMatch teamInMatch)
    {
        return true;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String getUniqueId(Player player)
    {
        return player.getId();
    }
}
