package uk.org.sehicl.website.page;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.LeagueTable;
import uk.org.sehicl.website.rules.Rules;

public class LeagueTablePage extends Page
{
    private final LeagueTable table;
    private final String title;
    private final boolean current;
    
    public LeagueTablePage(String leagueId, String uri)
    {
        super("table", "leaguetable.ftlh", Section.TABLES, uri);
        final League league = ModelLoader.getModel().getLeague(leagueId);
        table = new LeagueTable(league, new Rules.Builder().build());
        title = String.format("%s Table", league.getName());
        current = true;
    }
    
    public LeagueTablePage(String leagueId, int season, String uri)
    {
        super("table", "leaguetable.ftlh", Section.ARCHIVE, uri);
        final League league = ModelLoader.getModel(season).getLeague(leagueId);
        table = new LeagueTable(league, new Rules.Builder(season).build());
        title = String.format("%s Table - Season %d-%02d", league.getName(), season+1999, season);
        current = false;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    public LeagueTable getTable()
    {
        return table;
    }

    public boolean isCurrent()
    {
        return current;
    }
}
