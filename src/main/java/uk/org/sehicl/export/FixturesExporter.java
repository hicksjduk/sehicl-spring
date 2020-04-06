package uk.org.sehicl.export;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Team;
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
        final Map<String, String> teamNamesById = l
                .getTeams()
                .stream()
                .collect(Collectors.toMap(Team::getId, Team::getName));
        return l
                .getMatches()
                .stream()
                .map(m -> matchData(m.getDateTime(), m.getCourt(), divNum,
                        teamNamesById.get(m.getHomeTeamId()),
                        teamNamesById.get(m.getAwayTeamId())));
    }

    private String matchData(Date dateTime, String court, String divNum, String homeTeamName,
            String awayTeamName)
    {
        return String
                .format("%s,%s,%s,%s,%s,,,%s", DATE_FORMATTER.format(dateTime),
                        TIME_FORMATTER.format(dateTime), court, divNum, homeTeamName, awayTeamName);
    }
}
