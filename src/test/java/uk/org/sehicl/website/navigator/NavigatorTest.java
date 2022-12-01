package uk.org.sehicl.website.navigator;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.Test;

import uk.org.sehicl.website.navigator.Navigator.NavigatorRow;

public class NavigatorTest
{

    @Test
    public void testSingleItemSectionCurrent()
    {
        final List<NavigatorRow> results = new Navigator(Section.RULES, "/rules").getRows();
        final Stream<Consumer<NavigatorRow>> expected = Stream.of(validator("Home", "/"),
                validator("Contacts", "/contacts"), validator("Fixtures", "/fixtures"),
                validator("Results", "/results"), validator("Tables", "/tables"),
                validator("Averages", "/averages"), validator("Resources", "/resources"),
                validator("Rules", null), validator("Records", "/records"),
                validator("Archive", "/archive"), validator("Data Protection", "/dp"));
        validate(expected, results);
    }

    @Test
    public void testMultipleItemSectionCurrent()
    {
        final List<NavigatorRow> results = new Navigator(Section.FIXTURES, "/fixtures").getRows();
        final Stream<Consumer<NavigatorRow>> expected = Stream.of(validator("Home", "/"),
                validator("Contacts", "/contacts"),
                validator("Fixtures", null, validator("Division 1", "/fixtures/league/Division1"),
                        validator("Division 2", "/fixtures/league/Division2"),
                        validator("Division 3", "/fixtures/league/Division3"),
                        validator("Division 4", "/fixtures/league/Division4"),
                        validator("Division 5", "/fixtures/league/Division5"),
                        validator("Colts Under-16", "/fixtures/league/ColtsUnder16"),
                        validator("Colts Under-13", "/fixtures/league/ColtsUnder13"),
                        validator("Duty team rota", "/dutyRota")),
                validator("Results", "/results"), validator("Tables", "/tables"),
                validator("Averages", "/averages"), validator("Resources", "/resources"),
                validator("Rules", "/rules"), validator("Records", "/records"),
                validator("Archive", "/archive"), validator("Data Protection", "/dp"));
        validate(expected, results);
    }

    @Test
    public void testMultipleItemSubSectionCurrent()
    {
        final List<NavigatorRow> results = new Navigator(Section.CONTACTS, "/fullContacts")
                .getRows();
        final Stream<Consumer<NavigatorRow>> expected = Stream.of(validator("Home", "/"),
                validator("Contacts", "/contacts", validator("Full details", null)),
                validator("Fixtures", "/fixtures"), validator("Results", "/results"),
                validator("Tables", "/tables"), validator("Averages", "/averages"),
                validator("Resources", "/resources"), validator("Rules", "/rules"),
                validator("Records", "/records"), validator("Archive", "/archive"),
                validator("Data Protection", "/dp"));
        validate(expected, results);
    }

    @Test
    public void testMultipleNoItemCurrent()
    {
        final List<NavigatorRow> results = new Navigator(Section.AVERAGES,
                "/averages/team/OPCSTitchfield").getRows();
        final Stream<Consumer<NavigatorRow>> expected = Stream.of(validator("Home", "/"),
                validator("Contacts", "/contacts"), validator("Fixtures", "/fixtures"),
                validator("Results", "/results"), validator("Tables", "/tables"),
                validator("Averages", "/averages",
                        validator("Senior Batting", "/averages/batting/Senior"),
                        validator("Senior Bowling", "/averages/bowling/Senior"),
                        validator("Colts Under-16 Batting", "/averages/batting/ColtsUnder16"),
                        validator("Colts Under-16 Bowling", "/averages/bowling/ColtsUnder16"),
                        validator("Colts Under-13 Batting", "/averages/batting/ColtsUnder13"),
                        validator("Colts Under-13 Bowling", "/averages/bowling/ColtsUnder13"),
                        validator("By team", "/averages/byTeam")),
                validator("Resources", "/resources"), validator("Rules", "/rules"),
                validator("Records", "/records"), validator("Archive", "/archive"),
                validator("Data Protection", "/dp"));
        validate(expected, results);
    }

    @SafeVarargs
    private final Consumer<NavigatorRow> validator(String title, String uri,
            Consumer<NavigatorRow>... subItems)
    {
        return item ->
        {
            assertEquals(title, item.getTitle());
            assertEquals(uri, item.getUri());
            Arrays.stream(subItems).forEach(
                    v -> validate(Arrays.stream(subItems), item.getSubRows()));
        };
    }

    private <T> void validate(Stream<Consumer<T>> validators, Collection<T> objects)
    {
        if (objects == null)
        {
            assertEquals(0L, validators.count());
        }
        else
        {
            Iterator<T> iterator = objects.iterator();
            validators.forEach(v ->
            {
                assertTrue(iterator.hasNext());
                v.accept(iterator.next());
            });
            assertFalse(iterator.hasNext());
        }
    }
}
