package uk.org.sehicl.website.navigator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Navigator
{
    private final List<NavigatorRow> rows;

    public Navigator(Section section, String uri)
    {
        rows = Arrays.stream(NavigatorSection.values()).map(s -> toRow(section, uri, s)).collect(
                Collectors.toList());
    }

    public List<NavigatorRow> getRows()
    {
        return rows;
    }

    private NavigatorRow toRow(Section section, String uri, NavigatorSection navigatorSection)
    {
        return toRow(Objects.equals(section, navigatorSection.getSection()), uri,
                navigatorSection.getItem());
    }

    private NavigatorRow toRow(boolean current, String uri, NavigatorItem item)
    {
        NavigatorRow answer = new NavigatorRow(item.getTitle(),
                Objects.equals(uri, item.getUri()) ? null : item.getUri(),
                current ? item.getSubItems().stream().map(si -> toRow(current, uri, si)) : null);
        return answer;

    }

    public static class NavigatorRow
    {
        private final String title;
        private final String uri;
        private final List<NavigatorRow> subRows;

        public NavigatorRow(String title, String uri, Stream<NavigatorRow> subRows)
        {
            this.title = title;
            this.uri = uri;
            this.subRows = subRows == null ? Arrays.asList() : subRows.collect(Collectors.toList());
        }

        public String getTitle()
        {
            return title;
        }

        public String getUri()
        {
            return uri;
        }

        protected List<NavigatorRow> getSubRows()
        {
            return subRows;
        }

        @Override
        public String toString()
        {
            return "NavigatorRow [title=" + title + ", uri=" + uri + ", subRows=" + subRows + "]";
        }

    }
}
