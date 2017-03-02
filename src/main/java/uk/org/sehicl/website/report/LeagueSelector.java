package uk.org.sehicl.website.report;

import java.util.regex.Pattern;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.TeamInMatch;

public enum LeagueSelector implements AveragesSelector
{
    SENIOR("Division\\d"), UNDER16("ColtsUnder16"), UNDER13("ColtsUnder13");
    
    private final Pattern leagueIdPattern;
    
    private LeagueSelector(String regex)
    {
        leagueIdPattern = Pattern.compile(regex);
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
    public boolean isSelected(TeamInMatch teamInMatch, boolean batting)
    {
        return true;
    }
}
