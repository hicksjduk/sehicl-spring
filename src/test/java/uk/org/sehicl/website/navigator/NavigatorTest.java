package uk.org.sehicl.website.navigator;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.Test;

import uk.org.sehicl.website.navigator.Navigator.NavigatorRow;

public class NavigatorTest
{

    @Test
    public void testSingleItemSectionCurrent()
    {
        Iterator<NavigatorRow> results = new Navigator(Section.RULES, "/rules")
                .getRows()
                .iterator();
        final Stream<Consumer<NavigatorRow>> expected = Stream.of(validator("Home", "/", 0),
                validator("Contacts", "/contacts", 0), validator("Fixtures", "/fixtures", 0),
                validator("Results", "/results", 0), validator("Tables", "/tables", 0),
                validator("Averages", "/averages", 0), validator("Resources", "/resources", 0),
                validator("Rules", null, 0), validator("Records", "/records", 0),
                validator("Archive", "/archive", 0));
        expected.forEach(v -> v.accept(results.next()));
        assertFalse(results.hasNext());
    }

    @Test
    public void testMultipleItemSectionCurrent()
    {
        Iterator<NavigatorRow> results = new Navigator(Section.FIXTURES, "/fixtures")
                .getRows()
                .iterator();
        final Stream<Consumer<NavigatorRow>> expected = Stream.of(validator("Home", "/", 0),
                validator("Contacts", "/contacts", 0), validator("Fixtures", null, 0),
                validator("Division 1", "/fixtures/league/Division1", 1),
                validator("Division 2", "/fixtures/league/Division2", 1),
                validator("Division 3", "/fixtures/league/Division3", 1),
                validator("Division 4", "/fixtures/league/Division4", 1),
                validator("Colts Under-16", "/fixtures/league/ColtsUnder16", 1),
                validator("Colts Under-13", "/fixtures/league/ColtsUnder13", 1),
                validator("Duty team rota", "/dutyRota", 1), validator("Results", "/results", 0),
                validator("Tables", "/tables", 0), validator("Averages", "/averages", 0),
                validator("Resources", "/resources", 0), validator("Rules", "/rules", 0),
                validator("Records", "/records", 0), validator("Archive", "/archive", 0));
        expected.forEach(v -> v.accept(results.next()));
        assertFalse(results.hasNext());
    }

    @Test
    public void testMultipleItemSubSectionCurrent()
    {
        Iterator<NavigatorRow> results = new Navigator(Section.CONTACTS, "/fullContacts")
                .getRows()
                .iterator();
        final Stream<Consumer<NavigatorRow>> expected = Stream.of(validator("Home", "/", 0),
                validator("Contacts", "/contacts", 0), validator("Full details", null, 1),
                validator("Fixtures", "/fixtures", 0), validator("Results", "/results", 0),
                validator("Tables", "/tables", 0), validator("Averages", "/averages", 0),
                validator("Resources", "/resources", 0), validator("Rules", "/rules", 0),
                validator("Records", "/records", 0), validator("Archive", "/archive", 0));
        expected.forEach(v -> v.accept(results.next()));
        assertFalse(results.hasNext());
    }

    @Test
    public void testMultipleNoItemCurrent()
    {
        Iterator<NavigatorRow> results = new Navigator(Section.AVERAGES,
                "/averages/team/PortsmouthB").getRows().iterator();
        final Stream<Consumer<NavigatorRow>> expected = Stream.of(validator("Home", "/", 0),
                validator("Contacts", "/contacts", 0), validator("Fixtures", "/fixtures", 0),
                validator("Results", "/results", 0), validator("Tables", "/tables", 0),
                validator("Averages", "/averages", 0),
                validator("Senior Batting", "/averages/batting/Senior", 1),
                validator("Senior Bowling", "/averages/bowling/Senior", 1),
                validator("Colts Under-16 Batting", "/averages/batting/ColtsUnder16", 1),
                validator("Colts Under-16 Bowling", "/averages/bowling/ColtsUnder16", 1),
                validator("Colts Under-13 Batting", "/averages/batting/ColtsUnder13", 1),
                validator("Colts Under-13 Bowling", "/averages/bowling/ColtsUnder13", 1),
                validator("By team", "/averages/byTeam", 1),
                validator("Resources", "/resources", 0), validator("Rules", "/rules", 0),
                validator("Records", "/records", 0), validator("Archive", "/archive", 0));
        expected.forEach(v -> v.accept(results.next()));
        assertFalse(results.hasNext());
    }

    private Consumer<NavigatorRow> validator(String title, String uri, Consumer<NavigatorRow>... subItems)
    {
        return item ->
        {
            assertEquals(title, item.getTitle());
            assertEquals(uri, item.getUri());
            if ()
            Arrays.stream(subItems).forEach(action);
        };
    }
}
