package uk.org.sehicl.website.navigator;

import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import uk.org.sehicl.website.Constants;

public enum NavigatorSection
{
    HOME(Section.HOME, "Home", "/"),
    CONTACTS(Section.CONTACTS, "Contacts", "/contacts",
            new NavigatorItem("Full details", "/fullContacts")),
    FIXTURES(Section.FIXTURES, "Fixtures", "/fixtures", fixturesSubItems()),
    RESULTS(Section.RESULTS, "Results", "/results", resultsSubItems()),
    TABLES(Section.TABLES, "Tables", "/tables", tablesSubItems()),
    AVERAGES(Section.AVERAGES, "Averages", "/averages", averagesSubItems()),
    RESOURCES(Section.RESOURCES, "Resources", "/resources"),
    RULES(Section.RULES, "Rules", "/rules"),
    RECORDS(Section.RECORDS, "Records", "/records", recordsSubItems()),
    ARCHIVE(Section.ARCHIVE, "Archive", "/archive", archiveSubItems()),
    DP(Section.DP, "Data Protection", "/dp");

    private static NavigatorItem[] fixturesSubItems()
    {
        Stream.Builder<NavigatorItem> builder = Stream.builder();
        divisionSubItems("/fixtures/league/%s").forEach(builder);
        builder.accept(new NavigatorItem("Duty team rota", "/dutyRota"));
        return builder.build().toArray(NavigatorItem[]::new);
    }

    private static NavigatorItem[] resultsSubItems()
    {
        return divisionSubItems("/results/league/%s").toArray(NavigatorItem[]::new);
    }

    private static NavigatorItem[] tablesSubItems()
    {
        return divisionSubItems("/tables/league/%s").toArray(NavigatorItem[]::new);
    }

    private static Stream<NavigatorItem> divisionSubItems(String uriTemplate)
    {
        return IntStream
                .of(1, 2, 3, 4, 5, 6, 16, 13)
                .mapToObj(div -> String.format(div < 10 ? "Division %d" : "Colts Under-%d", div))
                .map(divName -> new NavigatorItem(divName,
                        String.format(uriTemplate, divName.replaceAll("\\W+", ""))));
    }

    private static NavigatorItem[] averagesSubItems()
    {
        Stream.Builder<NavigatorItem> builder = Stream.builder();
        BiFunction<String, String, NavigatorItem> itemMaker = (section, discipline) ->
        {
            final String title = String.format("%s %s", section, discipline);
            final String url = String.format("/averages/%s/%s", discipline.toLowerCase(),
                    section.replaceAll("\\W+", ""));
            return new NavigatorItem(title, url);
        };
        Stream
                .of("Senior", "Colts Under-16", "Colts Under-13")
                .flatMap(section -> Stream.of("Batting", "Bowling").map(
                        discipline -> itemMaker.apply(section, discipline)))
                .forEach(builder);
        builder.accept(new NavigatorItem("By team", "/averages/byTeam"));
        return builder.build().toArray(NavigatorItem[]::new);
    }

    private static NavigatorItem[] recordsSubItems()
    {
        return Stream
                .of(new NavigatorItem("Record Performances", "/records/performances"),
                        new NavigatorItem("Divisional Winners", "/records/winners"),
                        new NavigatorItem("Individual Awards", "/records/awards"),
                        new NavigatorItem("Sporting and Efficiency", "/records/fairplay"))
                .toArray(NavigatorItem[]::new);
    }

    private static NavigatorItem[] archiveSubItems()
    {
        final NavigatorItem[] answer = IntStream
                .range(4, Constants.CURRENT_SEASON)
                .mapToObj(s -> new NavigatorItem(String.format("%d-%02d", s + 1999, s),
                        String.format("/archive/season/%d", s)))
                .toArray(NavigatorItem[]::new);
        ArrayUtils.reverse(answer);
        return answer;
    }

    private final Section section;
    private final NavigatorItem item;

    private NavigatorSection(Section section, String title)
    {
        this(section, new NavigatorItem(title));
    }

    private NavigatorSection(Section section, String title, String url)
    {
        this(section, new NavigatorItem(title, url));
    }

    private NavigatorSection(Section section, String title, NavigatorItem... subItems)
    {
        this(section, new NavigatorItem(title, subItems));
    }

    private NavigatorSection(Section section, String title, String url, NavigatorItem... subItems)
    {
        this(section, new NavigatorItem(title, url, subItems));
    }

    private NavigatorSection(Section section, NavigatorItem item)
    {
        this.section = section;
        this.item = item;
    }

    public Section getSection()
    {
        return section;
    }

    public NavigatorItem getItem()
    {
        return item;
    }
}
