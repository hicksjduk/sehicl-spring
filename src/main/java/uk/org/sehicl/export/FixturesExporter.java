package uk.org.sehicl.export;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.dataload.ModelLoader;

public class FixturesExporter
{
    public static void main(String[] args) throws Exception
    {
        try (FileWriter w = new FileWriter(
                "/Users/jerhicks/Documents/personal/sehicl/fixtures.csv"))
        {
            new FixturesExporter().export(new PrintWriter(w));
        }
    }

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");

    private void export(PrintWriter pw) throws Exception
    {
        ModelLoader
                .getModel()
                .getLeagues()
                .stream()
                .flatMap(l -> l.getMatches().stream().map(m -> matchData(l, m)))
                .forEach(pw::println);
    }

    private String matchData(League l, Match m)
    {
        final String divNum = l.getId().replaceAll("[^\\d]", "");
        return String.format("%s,%s,%s,%s,%s,,,%s", DATE_FORMATTER.format(m.getDateTime()),
                TIME_FORMATTER.format(m.getDateTime()), m.getCourt(), divNum,
                l.getTeam(m.getHomeTeamId()).getName(), l.getTeam(m.getAwayTeamId()).getName());
    }
}
