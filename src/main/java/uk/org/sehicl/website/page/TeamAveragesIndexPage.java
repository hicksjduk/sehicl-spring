package uk.org.sehicl.website.page;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;

public class TeamAveragesIndexPage extends Page
{
    private final SortedMap<League, SortedSet<Team>> teamsByLeague;

    public TeamAveragesIndexPage(String uri)
    {
        super("averages", "teamaveragesindex.ftlh", Section.AVERAGES, uri);
        final Model model = ModelLoader.getModel();
        teamsByLeague = new TreeMap<>();
        model.getLeagues().stream().forEach(l -> teamsByLeague.put(l, new TreeSet<>(l.getTeams())));
    }

    @Override
    public String getTitle()
    {
        return "SEHICL Averages: By team";
    }

    public SortedMap<League, SortedSet<Team>> getTeamsByLeague()
    {
        return teamsByLeague;
    }
}
