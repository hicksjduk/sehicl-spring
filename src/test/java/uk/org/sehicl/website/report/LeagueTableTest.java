package uk.org.sehicl.website.report;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.report.LeagueTable.TableRow;
import uk.org.sehicl.website.report.ReportStatus.Status;
import uk.org.sehicl.website.rules.Rules;

public class LeagueTableTest
{
    @Test
    public void testEmptyLeague()
    {
        League league = new League();
        final LeagueTable result = new LeagueTable.Builder(league, new Rules.Builder(16).build(),
                Completeness.COMPLETE).build();
        checkTable(result, validate(league, null, Status.UNSTARTED, 0));
    }

    @Test
    public void testLeagueWithTeamsButNoMatches()
    {
        League league = new League();
        Stream.of("D", "E", "A", "B", "C").map(s ->
        {
            Team t = new Team();
            t.setId(s);
            t.setName("Team " + s);
            return t;
        }).forEach(league::addTeam);
        final LeagueTable result = new LeagueTable.Builder(league, new Rules.Builder(16).build(),
                Completeness.COMPLETE).build();
        checkTable(result, validate(league, null, Status.UNSTARTED, 0),
                validate("Team A", 0, 0, 0, 0, 0, 0, null, 0, 0),
                validate("Team B", 0, 0, 0, 0, 0, 0, null, 0, 0),
                validate("Team C", 0, 0, 0, 0, 0, 0, null, 0, 0),
                validate("Team D", 0, 0, 0, 0, 0, 0, null, 0, 0),
                validate("Team E", 0, 0, 0, 0, 0, 0, null, 0, 0));
    }

    @Test
    public void testCompleteLeagueWithPointsDeductions()
    {
        final Model model = ModelLoader.getModel(16);
        League league = model.getLeague("Division2");
        final LeagueTable result = new LeagueTable.Builder(league, new Rules.Builder(16).build(),
                Completeness.COMPLETE).build();
        checkTable(result,
                validate(league, getDate("2016-03-13"), Status.FINAL, 0,
                        "Failed to fulfil duty rota for 2 matches on 8th Nov - 2 points deducted",
                        "Failed to fulfil duty rota for 8 matches on 11th Oct - 8 points deducted"),
                validate("Purbrook", 9, 8, 0, 1, 48, 48, 10.57, 0, 192),
                validate("Portsmouth B", 9, 8, 0, 1, 49, 46, 10.01, 0, 191),
                validate("Waterlooville B", 9, 6, 0, 3, 52, 43, 10.87, 2, 165, 1),
                validate("Hayling Island", 9, 4, 0, 5, 42, 37, 9.68, 0, 127),
                validate("Emsworth", 9, 4, 0, 5, 37, 37, 8.79, 0, 122),
                validate("Locks Heath A", 9, 4, 0, 5, 35, 36, 8.79, 0, 119),
                validate("Bedhampton B", 9, 2, 0, 7, 46, 35, 10.15, 0, 105),
                validate("Portchester", 9, 3, 0, 6, 31, 36, 7.94, 0, 103),
                validate("Petersfield", 9, 4, 0, 5, 36, 26, 9.01, 8, 102, 2),
                validate("XIIth Men A", 9, 2, 0, 7, 32, 37, 8.08, 0, 93));
    }

    @Test
    public void testIncompleteLeague()
    {
        final Model model = ModelLoader.getModel(17);
        League league = model.getLeague("Division1");
        final LeagueTable result = new LeagueTable.Builder(league, new Rules.Builder(16).build(),
                Completeness.COMPLETE).build();
        checkTable(result, validate(league, getDate("2017-02-12"), Status.IN_PROGRESS, 1),
                validate("Portsmouth A", 7, 6, 0, 1, 33, 38, 10.72, 0, 143),
                validate("Hambledon A", 7, 6, 0, 1, 34, 35, 10.43, 0, 141),
                validate("Havant A", 7, 5, 0, 2, 38, 35, 10.40, 0, 133),
                validate("Waterlooville A", 6, 5, 0, 1, 32, 26, 9.92, 0, 118),
                validate("Fareham & Crofton A", 6, 3, 0, 3, 34, 29, 11.09, 0, 99),
                validate("Portsmouth B", 7, 1, 0, 6, 37, 31, 10.32, 0, 80),
                validate("Purbrook", 6, 2, 0, 4, 27, 25, 9.23, 0, 76),
                validate("Bedhampton A", 6, 2, 0, 4, 25, 24, 8.99, 0, 73),
                validate("Waterlooville B", 6, 1, 0, 5, 17, 22, 7.44, 0, 51),
                validate("Havant B", 6, 1, 0, 5, 20, 13, 8.12, 0, 45));
    }

    @SafeVarargs
    private final void checkTable(LeagueTable table, Consumer<LeagueTable> tableValidator,
            Consumer<TableRow>... rowValidators)
    {
        tableValidator.accept(table);
        final Iterator<TableRow> rowIterator = table.getRows().stream().iterator();
        Arrays.stream(rowValidators).forEach(v -> v.accept(rowIterator.next()));
        assertThat(rowIterator.hasNext()).isFalse();
    }

    private Consumer<LeagueTable> validate(League league, Date lastIncludedDate, Status status,
            int toCome, String... deductionDescriptions)
    {
        return table ->
        {
            assertThat(table.getLeague()).isSameAs(league);
            assertThat(table.getLastIncludedDate()).isEqualTo(lastIncludedDate);
            assertThat(table.getStatus()).isEqualTo(status);
            assertThat(table.getToCome()).isEqualTo(toCome);
            final Iterator<String> descIterator = table.getDeductionStrings().iterator();
            Arrays
                    .stream(deductionDescriptions)
                    .forEach(str -> assertThat(descIterator.next()).isEqualTo(str));
            assertThat(descIterator.hasNext()).isFalse();
        };
    }

    private Consumer<TableRow> validate(String teamName, int played, int won, int tied, int lost,
            int batPoints, int bowlPoints, Double runRate, int pointsDeducted, int points,
            Integer... deductionKeys)
    {
        return row ->
        {
            assertThat(row.getTeam().getName()).isEqualTo(teamName);
            assertThat(row.getPlayed()).isEqualTo(played);
            assertThat(row.getWon()).isEqualTo(won);
            assertThat(row.getTied()).isEqualTo(tied);
            assertThat(row.getLost()).isEqualTo(lost);
            assertThat(row.getBattingPoints()).isEqualTo(batPoints);
            assertThat(row.getBowlingPoints()).isEqualTo(bowlPoints);
            if (runRate == null)
            {
                assertNull(row.getRunRate());
            }
            else
            {
                assertThat(row.getRunRate()).isEqualTo(runRate, offset(0.01));
            }
            assertThat(row.getPointsDeducted()).isEqualTo(pointsDeducted);
            assertThat(row.getPoints()).isEqualTo(points);
            final Iterator<Integer> dkIterator = row.getDeductionKeys().iterator();
            Arrays.stream(deductionKeys).forEach(k -> assertThat(dkIterator.next()).isEqualTo(k));
            assertFalse(dkIterator.hasNext());
        };
    }

    private Date getDate(String isoDate)
    {
        try
        {
            return DateUtils.truncate(DateUtils.parseDate(isoDate, "yyyy-MM-dd"), Calendar.DATE);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }
}
