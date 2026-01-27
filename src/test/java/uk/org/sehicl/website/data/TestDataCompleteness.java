package uk.org.sehicl.website.data;

import static org.assertj.core.api.Assertions.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import uk.org.sehicl.website.Constants;
import uk.org.sehicl.website.report.ModelAndRules;
import uk.org.sehicl.website.rules.Rules;

class TestDataCompleteness
{
    final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, String> teamNamesById;

    @ParameterizedTest
    @MethodSource
    void testAllMatchesComplete(int season)
    {
        var mr = new ModelAndRules(season);
        teamNamesById = mr.model
                .getLeagues()
                .stream()
                .map(League::getTeams)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Team::getId, Team::getName));
        mr.model
                .getLeagues()
                .stream()
                .map(League::getMatches)
                .flatMap(Collection::stream)
                .filter(m -> m.getPlayedMatch() != null)
                .forEach(m -> assertThat(completeness(m, mr.rules))
                        .as(matchString(m))
                        .isNotEqualTo(Completeness.INCOMPLETE));
    }

    String matchString(Match m)
    {
        return "%s %s v %s"
                .formatted(DF.format(m.getDateTime()), teamNamesById.get(m.getHomeTeamId()),
                        teamNamesById.get(m.getAwayTeamId()));
    }

    Completeness completeness(Match m, Rules rules)
    {
        return Stream
                .of(m.getCompleteness(rules), checkPerformances(m.getPlayedMatch()))
                .sorted()
                .findFirst()
                .get();
    }

    Completeness checkPerformances(PlayedMatch m)
    {
        if (m
                .getTeams()
                .stream()
                .map(TeamInMatch::getInnings)
                .flatMap(i -> Stream.of(i.getBatsmen(), i.getBowlers()))
                .anyMatch(Collection::isEmpty))
            return Completeness.INCOMPLETE;
        var battersByInnings = m
                .getTeams()
                .stream()
                .map(TeamInMatch::getInnings)
                .map(i -> i.getBatsmen().stream().map(Performance::getPlayerId).toList())
                .toList();
        var bowlersByInnings = m
                .getTeams()
                .stream()
                .map(TeamInMatch::getInnings)
                .map(i -> i.getBowlers().stream().map(Performance::getPlayerId).toList())
                .toList();
        if (Stream
                .of(battersByInnings.get(0), bowlersByInnings.get(1))
                .flatMap(Collection::stream)
                .distinct()
                .count() > 6)
            return Completeness.INCOMPLETE;
        if (Stream
                .of(battersByInnings.get(1), bowlersByInnings.get(0))
                .flatMap(Collection::stream)
                .distinct()
                .count() > 6)
            return Completeness.INCOMPLETE;
        return Completeness.CONSISTENT;
    }

    static Stream<Arguments> testAllMatchesComplete()
    {
        return Stream.of(Arguments.of(Constants.CURRENT_SEASON));
    }
}
