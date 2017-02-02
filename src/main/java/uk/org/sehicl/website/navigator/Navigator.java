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
        rows = Arrays
                .stream(NavigatorSection.values())
                .flatMap(s -> toRows(section, uri, s))
                .collect(Collectors.toList());
    }

    public List<NavigatorRow> getRows()
    {
        return rows;
    }

    private Stream<NavigatorRow> toRows(Section section, String uri,
            NavigatorSection navigatorSection)
    {
        return toRows(Objects.equals(section, navigatorSection.getSection()), uri,
                navigatorSection.getItem(), 0);
    }

    private Stream<NavigatorRow> toRows(boolean current, String uri, NavigatorItem navigatorItem,
            int nestingLevel)
    {
        Stream<NavigatorRow> answer = Stream.of(new NavigatorRow(navigatorItem.getTitle(),
                Objects.equals(uri, navigatorItem.getUri()) ? null : navigatorItem.getUri(),
                nestingLevel));
        if (current)
        {
            answer = Stream.concat(answer, navigatorItem.getSubItems().stream().flatMap(
                    i -> toRows(current, uri, i, nestingLevel + 1)));
        }
        return answer;

    }

    public static class NavigatorRow
    {
        private final String title;
        private final String uri;
        private final int nestingLevel;

        public NavigatorRow(String title, String uri, int nestingLevel)
        {
            this.title = title;
            this.uri = uri;
            this.nestingLevel = nestingLevel;
        }

        public String getTitle()
        {
            return title;
        }

        public String getUri()
        {
            return uri;
        }

        public int getNestingLevel()
        {
            return nestingLevel;
        }

        @Override
        public String toString()
        {
            return "NavigatorRow [title=" + title + ", uri=" + uri + ", nestingLevel="
                    + nestingLevel + "]";
        }

    }
}
