package uk.org.sehicl.website.report;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.time.DateUtils;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.rules.Rules;

public class DateResults extends Results
{
    private final Date date;
    private final Collection<Date> allDates;

    private DateResults(Builder builder)
    {
        super(builder.results);
        allDates = builder.allDates;
        date = builder.date != null ? builder.date
                : allDates.isEmpty() ? null : allDates.iterator().next();
    }

    public Date getDate()
    {
        return date;
    }

    public Collection<Date> getAllDates()
    {
        return allDates;
    }

    public static class Builder
    {
        private final Model model;
        private final Date date;
        private final SortedSet<Date> allDates = new TreeSet<>((a, b) -> b.compareTo(a));
        private final Completeness completenessThreshold;
        private final Rules rules;
        private final Set<ResultDetails> results = new HashSet<>();

        public Builder(Model model, Date date, Completeness completenessThreshold, Rules rules)
        {
            this.model = model;
            this.date = date == null ? null : DateUtils.truncate(date, Calendar.DATE);
            this.completenessThreshold = completenessThreshold;
            this.rules = rules;
        }

        public DateResults build()
        {
            model.getLeagues().stream().forEach(this::add);
            return new DateResults(this);
        }

        private void add(League league)
        {
            league
                    .getMatches()
                    .stream()
                    .filter(m -> completenessThreshold.compareTo(m.getCompleteness(rules)) <= 0)
                    .forEach(m -> add(league, m));
        }

        private void add(League league, Match match)
        {
            final Date matchDate = DateUtils.truncate(match.getDateTime(), Calendar.DATE);
            final boolean newDate = allDates.add(matchDate);
            boolean include;
            if (date == null)
            {
                include = allDates.headSet(matchDate).isEmpty();
                if (include && newDate)
                {
                    results.clear();
                }
            }
            else
            {
                include = date.compareTo(matchDate) == 0;
            }
            if (include)
            {
                results.add(match.getPlayedMatch() == null ? new AwardedMatchDetails(league, match)
                        : new PlayedResultDetails(league, match, rules));
            }
        }
    }
}
