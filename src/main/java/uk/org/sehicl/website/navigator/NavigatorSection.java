package uk.org.sehicl.website.navigator;

import java.util.stream.IntStream;

import org.apache.commons.lang3.ArrayUtils;

import uk.org.sehicl.website.Constants;

public enum NavigatorSection
{
    HOME(Section.HOME, "Home", "/"),
    CONTACTS(Section.CONTACTS, "Contacts", "/contacts",
            new NavigatorItem("Full details", "/fullContacts")),
    FIXTURES(Section.FIXTURES, "Fixtures", "/fixtures",
            new NavigatorItem("Division 1", "/fixtures/league/Division1"),
            new NavigatorItem("Division 2", "/fixtures/league/Division2"),
            new NavigatorItem("Division 3", "/fixtures/league/Division3"),
            new NavigatorItem("Division 4", "/fixtures/league/Division4"),
            new NavigatorItem("Division 5", "/fixtures/league/Division5"),
            new NavigatorItem("Colts Under-16", "/fixtures/league/ColtsUnder16"),
            new NavigatorItem("Colts Under-13", "/fixtures/league/ColtsUnder13"),
            new NavigatorItem("Duty team rota", "/dutyRota")),
    RESULTS(Section.RESULTS, "Results", "/results",
            new NavigatorItem("Division 1", "/results/league/Division1"),
            new NavigatorItem("Division 2", "/results/league/Division2"),
            new NavigatorItem("Division 3", "/results/league/Division3"),
            new NavigatorItem("Division 4", "/results/league/Division4"),
            new NavigatorItem("Division 5", "/results/league/Division5"),
            new NavigatorItem("Colts Under-16", "/results/league/ColtsUnder16"),
            new NavigatorItem("Colts Under-13", "/results/league/ColtsUnder13")),
    TABLES(Section.TABLES, "Tables", "/tables",
            new NavigatorItem("Division 1", "/tables/league/Division1"),
            new NavigatorItem("Division 2", "/tables/league/Division2"),
            new NavigatorItem("Division 3", "/tables/league/Division3"),
            new NavigatorItem("Division 4", "/tables/league/Division4"),
            new NavigatorItem("Division 5", "/tables/league/Division5"),
            new NavigatorItem("Colts Under-16", "/tables/league/ColtsUnder16"),
            new NavigatorItem("Colts Under-13", "/tables/league/ColtsUnder13")),
    AVERAGES(Section.AVERAGES, "Averages", "/averages",
            new NavigatorItem("Senior Batting", "/averages/batting/Senior"),
            new NavigatorItem("Senior Bowling", "/averages/bowling/Senior"),
            new NavigatorItem("Colts Under-16 Batting", "/averages/batting/ColtsUnder16"),
            new NavigatorItem("Colts Under-16 Bowling", "/averages/bowling/ColtsUnder16"),
            new NavigatorItem("Colts Under-13 Batting", "/averages/batting/ColtsUnder13"),
            new NavigatorItem("Colts Under-13 Bowling", "/averages/bowling/ColtsUnder13"),
            new NavigatorItem("By team", "/averages/byTeam")),
    RESOURCES(Section.RESOURCES, "Resources", "/resources"),
    RULES(Section.RULES, "Rules", "/rules"),
    RECORDS(Section.RECORDS, "Records", "/records",
            new NavigatorItem("Record Performances", "/records/performances"),
            new NavigatorItem("Divisional Winners", "/records/winners"),
            new NavigatorItem("Individual Awards", "/records/awards"),
            new NavigatorItem("Sporting and Efficiency", "/records/fairplay")),
    ARCHIVE(Section.ARCHIVE, "Archive", "/archive",
            reverse(IntStream
                    .range(4, Constants.CURRENT_SEASON)
                    .mapToObj(s -> new NavigatorItem(String.format("%d-%02d", s + 1999, s),
                            String.format("/archive/season/%d", s)))
                    .toArray(NavigatorItem[]::new))),
    DP(Section.DP, "Data Protection", "/dp")
    ;
    
    private static <T> T[] reverse(T[] array)
    {
        ArrayUtils.reverse(array);
        return array;
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
