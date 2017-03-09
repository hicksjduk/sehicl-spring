package uk.org.sehicl.website.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

import uk.org.sehicl.website.data.Completeness;
import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Match;
import uk.org.sehicl.website.data.Model;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.rules.Rules;

public class TeamFixtures
{
    private final Team team;
    private final League league;
    private final Collection<Fixture> rows;

    private TeamFixtures(Builder builder)
    {
        rows = new TreeSet<>(builder.fixtures);
        team = builder.team;
        league = builder.league;
    }

    public Collection<Fixture> getRows()
    {
        return rows;
    }

    public Team getTeam()
    {
        return team;
    }

    public League getLeague()
    {
        return league;
    }

    public static class Fixture implements Comparable<Fixture>
    {
        private final Team opponent;
        private final Date dateTime;
        private final String court;
        private final boolean home;
        private final String result;

        public Fixture(League league, Team team, Match match, Completeness completenessThreshold,
                Rules rules)
        {
            home = Objects.equals(team.getId(), match.getHomeTeamId());
            String opponentId = home ? match.getAwayTeamId() : match.getHomeTeamId();
            opponent = league.getTeam(opponentId);
            dateTime = match.getDateTime();
            court = match.getCourt();
            result = completenessThreshold.compareTo(match.getCompleteness(rules)) <= 0
                    ? ResultFormatter.format(league, match, rules, team.getId()) : null;
        }

        public Team getOpponent()
        {
            return opponent;
        }

        public Date getDateTime()
        {
            return dateTime;
        }

        public String getCourt()
        {
            return court;
        }

        public String getResult()
        {
            return result;
        }

        public boolean isHome()
        {
            return home;
        }

        @Override
        public int compareTo(Fixture o)
        {
            int answer = 0;
            if (dateTime != o.dateTime)
            {
                answer = dateTime == null ? 1
                        : o.dateTime == null ? -1 : dateTime.compareTo(o.dateTime);
            }
            if (answer == 0)
            {
                answer = opponent.compareTo(o.opponent);
            }
            return answer;
        }
    }

    public static class Builder
    {
        private final Model model;
        private final String teamId;
        private Team team;
        private League league;
        private final Completeness completenessThreshold;
        private final Rules rules;
        private final List<Fixture> fixtures = new ArrayList<>();

        public Builder(Model model, String teamId, Completeness completenessThreshold, Rules rules)
        {
            this.model = model;
            this.teamId = teamId;
            this.completenessThreshold = completenessThreshold;
            this.rules = rules;
        }

        public TeamFixtures build()
        {
            for (League league : model.getLeagues())
            {
                Team team = league.getTeam(teamId);
                if (team != null)
                {
                    processLeague(league, team);
                    this.league = league;
                    this.team = team;
                    break;
                }
            }
            return new TeamFixtures(this);
        }

        private void processLeague(League league, Team team)
        {
            league
                    .getMatches()
                    .stream()
                    .filter(m -> Arrays
                            .asList(m.getHomeTeamId(), m.getAwayTeamId())
                            .contains(team.getId()))
                    .forEach(match -> fixtures
                            .add(new Fixture(league, team, match, completenessThreshold, rules)));
        }
    }
}
