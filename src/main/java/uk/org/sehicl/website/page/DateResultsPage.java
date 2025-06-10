package uk.org.sehicl.website.page;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.org.sehicl.website.OrdinalDateFormatter;
import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.report.DateResults;
import uk.org.sehicl.website.rules.Rules;

public class DateResultsPage extends Page
{
    private int getSeason(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) % 100 + (cal.get(Calendar.MONTH) > Calendar.JUNE ? 1 : 0);
    }

    private final DateResults results;
    private final String title;

    public DateResultsPage()
    {
        this(null);
    }

    public DateResultsPage(Date date)
    {
        super("results", "results.ftlh", Section.RESULTS);
        if (date == null)
        {
            results = new DateResults.Builder(ModelLoader.getModel(), date, Completeness.CONSISTENT,
                    new Rules.Builder().build()).build();
            title = "Results";
        }
        else
        {
            final int season = getSeason(date);
            results = new DateResults.Builder(ModelLoader.getModel(season), date,
                    Completeness.CONSISTENT, new Rules.Builder(season).build()).build();
            title = String.format("Results: %s",
                    new OrdinalDateFormatter(new SimpleDateFormat("d MMMM yyyy"))
                            .format(results.getDate()));
        }
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
