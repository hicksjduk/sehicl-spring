package uk.org.sehicl.website.navigator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Navigator
{
    private final Section section;
    private final String uri;
    private final List<NavigatorRow> rows;

    public Navigator(Section section, String uri)
    {
        this.section = section;
        this.uri = uri;
        rows = Arrays
                .stream(NavigatorSection.values())
                .map(s -> new NavigatorRowFactory(section, uri, s).toRow(s.getItem()))
                .collect(Collectors.toList());
    }

    private static class NavigatorRowFactory
    {
        private final String uri;
        private final boolean current;
        
        public NavigatorRowFactory(Section section, String uri, NavigatorSection navigatorSection)
        {
            this.uri = uri;
            this.current = Objects.equals(section, navigatorSection.getSection());
        }

        public NavigatorRow toRow(NavigatorItem item)
        {
            return new NavigatorRow(item.getTitle(),
                    Objects.equals(item.getUri(), this.uri) ? null : item.getUri(),
                    current ? item.getSubItems().stream().map(this::toRow) : null);
        }
        
    }

    private static class NavigatorRow
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

    }
}
