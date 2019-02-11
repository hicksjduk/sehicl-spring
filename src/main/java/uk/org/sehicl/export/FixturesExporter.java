package uk.org.sehicl.export;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.dataload.ModelLoader;

public class FixturesExporter
{
    public static void main(String[] args) throws Exception
    {
        final File dir = new File(System.getProperty("user.home"), "Documents/personal/sehicl");
        dir.mkdirs();
        final File file = new File(dir, "fixtures.csv");
        try (FileWriter w = new FileWriter(file))
        {
            new FixturesExporter().export(new PrintWriter(w));
        }
        System.out.println("Output written to " + file.getAbsolutePath());
    }

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");

    private void export(PrintWriter pw) throws Exception
    {
        ModelLoader.getModel().getLeagues().stream().flatMap(this::matchData).forEach(pw::println);
    }

    private Stream<String> matchData(League l)
    {
        final String divNum = l.getId().replaceAll("[^\\d]", "");
        return l.getMatches().stream().map(m -> String.format("%s,%s,%s,%s,%s,,,%s",
                DATE_FORMATTER.format(m.getDateTime()), TIME_FORMATTER.format(m.getDateTime()),
                m.getCourt(), divNum, l.getTeam(m.getHomeTeamId()).getName(),
                l.getTeam(m.getAwayTeamId()).getName()));
    }
}
