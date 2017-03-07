package uk.org.sehicl.website.page;

import uk.org.sehicl.website.navigator.Section;

public class SeasonArchiveIndexPage extends Page
{
    private final int season;

    public SeasonArchiveIndexPage(String uri, int season)
    {
        super("archive", "seasonarchiveindex.ftlh", Section.ARCHIVE, uri);
        this.season = season;
    }

    @Override
    public String getTitle()
    {
        return String.format("SEHICL Archive - Season %d-%02d", 1999 + season, season);
    }

    public int getSeason()
    {
        return season;
    }
}
