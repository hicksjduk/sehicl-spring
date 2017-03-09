package uk.org.sehicl.website.report;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.rules.Rules;

public class DateResults
{
    private DateResults(Builder builder)
    {

    }

    public static abstract class ResultDetails implements Comparable<ResultDetails>
    {
        private final League league;
        private final Match match;

        public ResultDetails(League league, Match match)
        {
            this.league = league;
            this.match = match;
        }

        @Override
        public int compareTo(ResultDetails o)
        {
            int answer = league.compareTo(o.league);
            if (answer == 0)
            {
                answer = match.compareTo(o.match);
            }
            return answer;
        }
    }
    
    public static class PlayedResultDetails extends ResultDetails
    {
        
    }

    public static class Builder
    {
        private final Model model;
        private final Date date;
        private final Set<Date> allDates = new HashSet<>();
        private final Completeness completenessThreshold;
        private final Rules rules;
        private final Set<ResultDetails> results = new HashSet<>();

        public Builder(Model model, Date date, Completeness completenessThreshold, Rules rules)
        {
            this.model = model;
            this.date = date;
            this.completenessThreshold = completenessThreshold;
            this.rules = rules;
        }

        public DateResults build()
        {
            return new DateResults(this);
        }
    }
}
