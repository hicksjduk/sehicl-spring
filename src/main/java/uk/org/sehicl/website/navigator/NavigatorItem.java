package uk.org.sehicl.website.navigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NavigatorItem
{
    private final String uri;
    private final String title;
    private final List<NavigatorItem> subItems;

    public NavigatorItem(String title)
    {
        this(title, null, new ArrayList<>());
    }

    public NavigatorItem(String title, String uri)
    {
        this(title, uri, new ArrayList<>());
    }

    public NavigatorItem(String title, NavigatorItem... subItems)
    {
        this(title, null, Arrays.asList(subItems));
    }

    public NavigatorItem(String title, String uri, NavigatorItem... subItems)
    {
        this(title, uri, Arrays.asList(subItems));
    }

    private NavigatorItem(String title, String uri, List<NavigatorItem> subItems)
    {
        this.title = title;
        this.uri = uri;
        this.subItems = Collections.unmodifiableList(subItems);
    }

    public String getUri()
    {
        return uri;
    }

    public String getTitle()
    {
        return title;
    }

    public List<NavigatorItem> getSubItems()
    {
        return subItems;
    }
}
