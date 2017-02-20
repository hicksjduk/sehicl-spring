package uk.org.sehicl.website.report;

import java.util.Calendar;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.rules.Rules;

public class ReportStatus
{
    public static enum Status
    {
        UNSTARTED, IN_PROGRESS, FINAL
    }

    private Date lastIncludedDate = null;
    private final SortedMap<Date, AtomicInteger> unplayedCountsByDate = new TreeMap<>(
            (d1, d2) -> d2.compareTo(d1));

    public void add(Match match, Rules rules, Completeness completenessThreshold)
    {
        add(match, rules, completenessThreshold.compareTo(match.getCompleteness(rules)) >= 0);
    }

    public void add(Match match, Rules rules, boolean complete)
    {
        if (match.getDateTime() != null)
        {
            Date date = DateUtils.truncate(match.getDateTime(), Calendar.DATE);
            if (complete)
            {
                if (lastIncludedDate == null || lastIncludedDate.before(date))
                {
                    lastIncludedDate = date;
                }
            }
            else
            {
                AtomicInteger count = unplayedCountsByDate.get(date);
                if (count == null)
                {
                    unplayedCountsByDate.put(date, new AtomicInteger(1));
                }
                else
                {
                    count.incrementAndGet();
                }
            }
        }
    }

    public Date getLastIncludedDate()
    {
        return lastIncludedDate;
    }

    public int getToCome()
    {
        int answer = 0;
        if (lastIncludedDate != null)
        {
            answer = unplayedCountsByDate.tailMap(lastIncludedDate).values().stream().collect(
                    Collectors.summingInt(AtomicInteger::get));
        }
        return answer;
    }

    public Status getStatus()
    {
        Status answer = lastIncludedDate == null ? Status.UNSTARTED
                : unplayedCountsByDate.isEmpty() ? Status.FINAL : Status.IN_PROGRESS;
        return answer;
    }
}
