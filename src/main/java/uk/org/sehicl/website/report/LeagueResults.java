package uk.org.sehicl.website.report;

import java.util.HashSet;
import java.util.Set;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.rules.Rules;

public class LeagueResults extends Results
{
    private final League league;

    private LeagueResults(Builder builder)
    {
        super(builder.results);
        league = builder.league;
    }

    public League getLeague()
    {
        return league;
    }

    public static class Builder
    {
        private final League league;
        private final Completeness completenessThreshold;
        private final Rules rules;
        private final Set<ResultDetails> results = new HashSet<>();

        public Builder(Model model, String leagueId, Completeness completenessThreshold,
                Rules rules)
        {
            this.league = model.getLeague(leagueId);
            this.completenessThreshold = completenessThreshold;
            this.rules = rules;
        }

        public LeagueResults build()
        {
            add(league);
            return new LeagueResults(this);
        }

        private void add(League league)
        {
            league
                    .getMatches()
                    .stream()
                    .filter(m -> completenessThreshold.compareTo(m.getCompleteness(rules)) <= 0)
                    .forEach(this::add);
        }

        private void add(Match match)
        {
            results.add(match.getPlayedMatch() == null ? new AwardedMatchDetails(league, match)
                    : new PlayedResultDetails(league, match, rules));
        }
    }
}
