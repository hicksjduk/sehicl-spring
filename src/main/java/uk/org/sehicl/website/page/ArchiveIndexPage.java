package uk.org.sehicl.website.page;

import uk.org.sehicl.website.Constants;
import uk.org.sehicl.website.navigator.Section;

public class ArchiveIndexPage extends Page
{
    private final int currentSeason = Constants.CURRENT_SEASON;

    public ArchiveIndexPage(String uri)
    {
        super("archive", "archiveindex.ftlh", Section.ARCHIVE, uri);
    }

    @Override
    public String getTitle()
    {
        return "SEHICL Archive";
    }

    public int getCurrentSeason()
    {
        return currentSeason;
    }
}
