package uk.org.sehicl.website.data;

import static org.assertj.core.api.Assertions.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import uk.org.sehicl.website.Constants;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.rules.Rules;

class TestDataCompleteness
{
    final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

    @ParameterizedTest
    @MethodSource
    void testAllMatchesComplete(int season)
    {
        var rules = new Rules.Builder(season).build();
        var model = ModelLoader.getModel(season);
        var teamNames = model
                .getLeagues()
                .stream()
                .map(League::getTeams)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Team::getId, Team::getName));
        model
                .getLeagues()
                .stream()
                .map(League::getMatches)
                .flatMap(Collection::stream)
                .filter(m -> m.getPlayedMatch() != null)
                .forEach(m -> assertThat(completeness(m, rules))
                        .as("%s %s v %s", DF.format(m.getDateTime()),
                                teamNames.get(m.getHomeTeamId()), teamNames.get(m.getAwayTeamId()))
                        .isNotEqualTo(Completeness.INCOMPLETE));
    }

    Completeness completeness(Match m, Rules rules)
    {
        return Stream
                .of(m.getCompleteness(rules), checkPerformances(m.getPlayedMatch(), rules))
                .sorted()
                .findFirst()
                .get();
    }

    Completeness checkPerformances(PlayedMatch m, Rules rules)
    {
        if (m
                .getTeams()
                .stream()
                .map(TeamInMatch::getInnings)
                .map(Innings::getPerformances)
                .anyMatch(Collection::isEmpty))
            return Completeness.INCOMPLETE;
        return Completeness.CONSISTENT;
    }

    static Stream<Arguments> testAllMatchesComplete()
    {
        return Stream.of(Arguments.of(Constants.CURRENT_SEASON));
    }
}
