package uk.org.sehicl.website.report;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.rules.Rules;

public class LeagueFixtures
{
    private final League league;
    private final Collection<UnplayedMatch> rows;
    private final boolean matchesExist;

    private LeagueFixtures(Builder builder)
    {
        league = builder.leagueId == null ? null : builder.model.getLeague(builder.leagueId);
        rows = builder.matches;
        matchesExist = builder.matchesExist;
    }

    public League getLeague()
    {
        return league;
    }

    public Collection<UnplayedMatch> getRows()
    {
        return rows;
    }

    public boolean isMatchesExist()
    {
        return matchesExist;
    }

    public static class UnplayedMatch implements Comparable<UnplayedMatch>
    {
        private static final Comparator<UnplayedMatch> COMPARATOR = Comparator
                .comparing(UnplayedMatch::getMatch,
                        Comparator
                                .comparing(Match::getDateTime,
                                        Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(Match::getCourt,
                                        Comparator.nullsLast(Comparator.naturalOrder())))
                .thenComparing(UnplayedMatch::getHomeTeam)
                .thenComparing(UnplayedMatch::getAwayTeam);
        
        private final League league;
        private final Match match;
        private final Team homeTeam;
        private final Team awayTeam;

        public UnplayedMatch(League league, Match match)
        {
            this.league = league;
            this.match = match;
            homeTeam = league.getTeam(match.getHomeTeamId());
            awayTeam = league.getTeam(match.getAwayTeamId());
        }

        public League getLeague()
        {
            return league;
        }

        public Match getMatch()
        {
            return match;
        }

        public Team getHomeTeam()
        {
            return homeTeam;
        }

        public Team getAwayTeam()
        {
            return awayTeam;
        }

        @Override
        public int compareTo(UnplayedMatch o)
        {
            return COMPARATOR.compare(this, o);
        }
    }

    public static class Builder
    {
        private final Model model;
        private final String leagueId;
        private final Collection<UnplayedMatch> matches = new TreeSet<>();
        private final Completeness completenessThreshold;
        private final Rules rules;
        private boolean matchesExist;

        public Builder(Model model, String leagueId, Completeness completenessThreshold,
                Rules rules)
        {
            this.model = model;
            this.leagueId = leagueId;
            this.completenessThreshold = completenessThreshold;
            this.rules = rules;
        }

        public LeagueFixtures build()
        {
            for (League league : model.getLeagues())
            {
                if (leagueId == null || leagueId.equals(league.getId()))
                {
                    add(league);
                    if (leagueId != null)
                    {
                        break;
                    }
                }
            }
            return new LeagueFixtures(this);
        }

        private void add(League league)
        {
            league
                    .getMatches()
                    .stream()
                    .peek(m -> matchesExist = true)
                    .filter(m -> completenessThreshold.compareTo(m.getCompleteness(rules)) > 0)
                    .forEach(m -> matches.add(new UnplayedMatch(league, m)));
        }
    }
}
