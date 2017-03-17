package uk.org.sehicl.website.page;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.org.sehicl.website.OrdinalDateFormatter;
import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.DateResults;
import uk.org.sehicl.website.rules.Rules;

public class DateResultsPage extends Page
{
    private final DateResults results;
    private final String title;

    public DateResultsPage(String uri)
    {
        this(null, uri);
    }

    public DateResultsPage(Date date, String uri)
    {
        super("results", "results.ftlh", Section.RESULTS, uri);
        results = new DateResults.Builder(ModelLoader.getModel(), date, Completeness.CONSISTENT,
                new Rules.Builder().build()).build();
        title = results.getDate() == null ? "Results"
                : String.format("Results: %s",
                        new OrdinalDateFormatter(new SimpleDateFormat("d MMMM yyyy"))
                                .format(results.getDate()));
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    public DateResults getResults()
    {
        return results;
    }

}
