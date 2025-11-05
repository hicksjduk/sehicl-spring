package uk.org.sehicl.website.data;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import uk.org.sehicl.website.Constants;
import uk.org.sehicl.website.dataload.ModelLoader;
import uk.org.sehicl.website.rules.Rules;

class TestDataCompleteness
{
    void test()
    {
        fail("Not yet implemented");
    }

    @ParameterizedTest
    @MethodSource
    void testAllMatchesComplete(int season)
    {
        var rules = new Rules.Builder(season).build();
        var model = ModelLoader.getModel(season);
        model
                .getLeagues()
                .stream()
                .map(League::getMatches)
                .flatMap(Collection::stream)
                .filter(m -> m.getPlayedMatch() != null)
                .forEach(m -> assertThat(m.getCompleteness(rules))
                        .as("%s", m)
                        .isNotEqualTo(Completeness.INCOMPLETE));
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
