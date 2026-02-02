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
                .of(m.getCompleteness(rules), checkPerformances(m.getPlayedMatch(), rules))
                .sorted()
                .findFirst()
                .get();
    }

    Completeness checkWickets(Innings i, Rules r)
    {
        var wkts = i.getWicketsLost(r.getMaxWickets());
        if (i.getBatsmen().stream().filter(Batsman::isOut).count() > wkts)
            return Completeness.INCOMPLETE;
        if (i.getBowlers().stream().mapToInt(Bowler::getWicketsTaken).sum() > wkts)
            return Completeness.INCOMPLETE;
        return Completeness.CONSISTENT;
    }

    Completeness checkPerformances(PlayedMatch m, Rules r)
    {
        var innings = m.getTeams().stream().map(TeamInMatch::getInnings).toList();
        if (innings
                .stream()
                .map(i -> checkWickets(i, r))
                .filter(Completeness.INCOMPLETE::equals)
                .findFirst()
                .isPresent())
            return Completeness.INCOMPLETE;
        if (innings
                .stream()
                .flatMap(i -> Stream.of(i.getBatsmen(), i.getBowlers()))
                .anyMatch(Collection::isEmpty))
            return Completeness.INCOMPLETE;
        var battersByInnings = innings
                .stream()
                .map(i -> i.getBatsmen().stream().map(Performance::getPlayerId).toList())
                .toList();
        var bowlersByInnings = innings
                .stream()
                .map(i -> i.getBowlers().stream().map(Performance::getPlayerId).toList())
                .toList();
        var playerCounts = IntStream
                .of(0, 1)
                .mapToObj(i -> Stream.of(battersByInnings.get(i), bowlersByInnings.get(1 - i)))
                .map(str -> str.flatMap(Collection::stream))
                .map(Stream::distinct)
                .mapToLong(Stream::count);
        if (playerCounts.anyMatch(c -> c > 6))
            return Completeness.INCOMPLETE;
        return Completeness.CONSISTENT;
    }

    static Stream<Arguments> testAllMatchesComplete()
    {
        return Stream.of(Arguments.of(Constants.CURRENT_SEASON));
    }
}
