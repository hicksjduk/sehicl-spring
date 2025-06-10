package uk.org.sehicl.website.page;

import java.util.ArrayList;
import java.util.List;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.LeagueTable;
import uk.org.sehicl.website.rules.Rules;

public class LeagueTablesPage extends Page
{
    private final List<LeagueTable> tables = new ArrayList<>();
    private final String title;
    private final boolean current;

    public LeagueTablesPage()
    {
        super("table", "leaguetable.ftlh", Section.TABLES);
        final Rules rules = new Rules.Builder().build();
        ModelLoader
                .getModel()
                .getLeagues()
                .stream()
                .sorted()
                .map(l -> new LeagueTable.Builder(l, rules, Completeness.CONSISTENT).build())
                .forEach(tables::add);
        title = "SEHICL Tables";
        current = true;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    public boolean isCurrent()
    {
        return current;
    }

    public List<LeagueTable> getTables()
    {
        return tables;
    }
}
