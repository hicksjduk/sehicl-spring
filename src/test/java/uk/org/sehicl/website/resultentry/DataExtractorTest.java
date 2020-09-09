package uk.org.sehicl.website.resultentry;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.Mockito.*;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.org.sehicl.website.resultentry.MatchData.BattingData;
import uk.org.sehicl.website.resultentry.MatchData.BowlingData;
import uk.org.sehicl.website.resultentry.MatchData.InningsData;
import uk.org.sehicl.website.rules.Rules;
import uk.org.thehickses.cartesian.CartesianProductBuilder;
import uk.org.thehickses.cartesian.Combination;

@ExtendWith(MockitoExtension.class)
class DataExtractorTest
{
    private DataExtractor extractor;

    @Mock
    private UnaryOperator<String> fieldExtractor;

    private final Rules rules = new Rules.Builder().build();

    @BeforeEach
    void setup()
    {
        extractor = new DataExtractor(fieldExtractor, rules);
    }

    @ParameterizedTest
    @MethodSource
    void testHomeTeamBattingFirst(String value, boolean expected)
    {
        when(fieldExtractor.apply("battingFirst")).thenReturn(value);
        assertThat(extractor.homeTeamBattingFirst()).isEqualTo(expected);
    }

    static Stream<Arguments> testHomeTeamBattingFirst()
    {
        return Stream
                .of(arguments(null, true), arguments("", true), arguments("    ", true),
                        arguments("asdas", false), arguments("hometeam", true),
                        arguments("awayteam", false));
    }

    @ParameterizedTest
    @MethodSource
    void testGetInnings(int sequence, String battingFirst, String extras, String runs,
            String wickets, String overs, InningsData expected)
    {
        when(fieldExtractor.apply("EXTRAS" + sequence)).thenReturn(extras);
        when(fieldExtractor.apply("TOTAL" + sequence)).thenReturn(runs);
        when(fieldExtractor.apply("WICKETS" + sequence)).thenReturn(wickets);
        when(fieldExtractor.apply("OVERS" + sequence)).thenReturn(overs);
        if (expected != null)
            when(fieldExtractor.apply("battingFirst")).thenReturn(battingFirst);
        InningsData result = extractor.getInnings(sequence);
        if (expected == null)
            assertThat(result).isNull();
        else
        {
            assertThat(result.isHomeTeamBatting()).isEqualTo(expected.isHomeTeamBatting());
            assertThat(result.getExtras()).isEqualTo(expected.getExtras());
            assertThat(result.getTotal()).isEqualTo(expected.getTotal());
            assertThat(result.getWickets()).isEqualTo(expected.getWickets());
            assertThat(result.getBalls()).isEqualTo(expected.getBalls());
        }
    }

    static Stream<Arguments> testGetInnings()
    {
        return CartesianProductBuilder
                .of(DataPair.of(null, null), DataPair.of("", null), DataPair.of("4", 4))
                .and(DataPair.of(null, null), DataPair.of("", null), DataPair.of("15", 15))
                .and(DataPair.of(null, null), DataPair.of("", null), DataPair.of("2", 2))
                .and(DataPair.of(null, null), DataPair.of("", null), DataPair.of("11.3", 69))
                .and(1, 2)
                .and("hometeam", "awayteam")
                .build()
                .map(DataExtractorTest::testGetInnings);
    }

    @SuppressWarnings("unchecked")
    static Arguments testGetInnings(Combination comb)
    {
        DataPair<Integer> extras = comb.next(DataPair.class);
        DataPair<Integer> total = comb.next(DataPair.class);
        DataPair<Integer> wickets = comb.next(DataPair.class);
        DataPair<Integer> overs = comb.next(DataPair.class);
        int sequence = comb.nextInt();
        String battingFirst = comb.next(String.class);
        String[] batFirstValues = { "hometeam", "awayteam" };
        boolean allMatch = Stream
                .of(extras, total, wickets, overs)
                .map(dp -> dp.source)
                .allMatch(StringUtils::isEmpty);
        InningsData expected = allMatch ? null
                : new InningsData(battingFirst.equals(batFirstValues[sequence - 1]), extras.target,
                        total.target, wickets.target, overs.target);
        return arguments(sequence, battingFirst, extras.source, total.source, wickets.source,
                overs.source, expected);
    }

    @ParameterizedTest
    @MethodSource
    void testGetBatting(int innings, int sequence, String batsman, String howOut, String runs,
            String bowler, BattingData expected)
    {
        when(fieldExtractor.apply("BATSMAN" + innings + sequence)).thenReturn(batsman);
        when(fieldExtractor.apply("HOW_OUT" + innings + sequence)).thenReturn(howOut);
        when(fieldExtractor.apply("WICKET_BOWLER" + innings + sequence)).thenReturn(bowler);
        when(fieldExtractor.apply("RUNS_SCORED" + innings + sequence)).thenReturn(runs);
        BattingData result = extractor.getBatting(innings, sequence);
        if (expected == null)
            assertThat(result).isNull();
        else
        {
            assertThat(result.getBatsman()).isEqualTo(expected.getBatsman());
            assertThat(result.getHowOut()).isEqualTo(expected.getHowOut());
            assertThat(result.getWicketBowler()).isEqualTo(expected.getWicketBowler());
            assertThat(result.getRuns()).isEqualTo(expected.getRuns());
        }
    }

    static Stream<Arguments> testGetBatting()
    {
        return CartesianProductBuilder
                .of(1, 2)
                .and(1, 2, 3, 4, 5, 6)
                .and(DataPair.of(null), DataPair.of(""), DataPair.of("Jeremy"))
                .and(DataPair.of(null, null), DataPair.of("", null),
                        DataPair.of("DID_NOT_BAT", HowOut.DID_NOT_BAT),
                        DataPair.of("BOWLED", HowOut.BOWLED))
                .and(DataPair.of(null, null), DataPair.of("", null), DataPair.of("25", 25))
                .and(DataPair.of(null), DataPair.of(""), DataPair.of("Peter"))
                .build()
                .map(DataExtractorTest::testGetBatting);
    }

    @SuppressWarnings("unchecked")
    static Arguments testGetBatting(Combination comb)
    {
        int sequence = comb.nextInt();
        int innings = comb.nextInt();
        DataPair<String> batsman = comb.next(DataPair.class);
        DataPair<HowOut> howOut = comb.next(DataPair.class);
        DataPair<Integer> score = comb.next(DataPair.class);
        DataPair<String> bowler = comb.next(DataPair.class);
        boolean dataFound = Stream
                .of(batsman.source, bowler.source, score.source)
                .anyMatch(StringUtils::isNotBlank)
                || (howOut.target != null && howOut.target != HowOut.DID_NOT_BAT);
        BattingData expected = dataFound
                ? new BattingData(batsman.target, howOut.target, bowler.target, score.target)
                : null;
        return arguments(innings, sequence, batsman.source, howOut.source, score.source,
                bowler.source, expected);
    }

    @ParameterizedTest
    @MethodSource
    void testGetBowling(int innings, int sequence, String bowler, String balls, String runs,
            String wickets, BowlingData expected)
    {
        when(fieldExtractor.apply("BOWLER" + innings + sequence)).thenReturn(bowler);
        when(fieldExtractor.apply("OVERS" + innings + sequence)).thenReturn(balls);
        when(fieldExtractor.apply("RUNS_CONCEDED" + innings + sequence)).thenReturn(runs);
        when(fieldExtractor.apply("WICKETS" + innings + sequence)).thenReturn(wickets);
        BowlingData result = extractor.getBowling(innings, sequence);
        if (expected == null)
            assertThat(result).isNull();
        else
        {
            assertThat(result.getBowler()).isEqualTo(expected.getBowler());
            assertThat(result.getBalls()).isEqualTo(expected.getBalls());
            assertThat(result.getRuns()).isEqualTo(expected.getRuns());
            assertThat(result.getWickets()).isEqualTo(expected.getWickets());
        }
    }

    static Stream<Arguments> testGetBowling()
    {
        return CartesianProductBuilder
                .of(1, 2)
                .and(1, 2, 3, 4, 5, 6)
                .and(DataPair.of(null), DataPair.of(""), DataPair.of("Jeremy"))
                .and(DataPair.of(null, null), DataPair.of("", null), DataPair.of("0", 0),
                        DataPair.of("14", 14))
                .and(DataPair.of(null, null), DataPair.of("", null), DataPair.of("22", 22))
                .and(DataPair.of(null, null), DataPair.of("", null), DataPair.of("3", 3))
                .build()
                .map(DataExtractorTest::testGetBowling);
    }

    @SuppressWarnings("unchecked")
    static Arguments testGetBowling(Combination comb)
    {
        int sequence = comb.nextInt();
        int innings = comb.nextInt();
        DataPair<String> bowler = comb.next(DataPair.class);
        DataPair<Integer> balls = comb.next(DataPair.class);
        DataPair<Integer> runs = comb.next(DataPair.class);
        DataPair<Integer> wickets = comb.next(DataPair.class);
        boolean dataFound = Stream
                .of(bowler.source, runs.source, wickets.source)
                .anyMatch(StringUtils::isNotBlank) || (balls.target != null && balls.target > 0);
        BowlingData expected = dataFound
                ? new BowlingData(bowler.target, balls.target, runs.target, wickets.target)
                : null;
        return arguments(innings, sequence, bowler.source, balls.source, runs.source,
                wickets.source, expected);
    }

    private static class DataPair<T>
    {
        public static DataPair<String> of(String source)
        {
            return new DataPair<>(source, StringUtils.isEmpty(source) ? null : source);
        }

        public static <T> DataPair<T> of(String source, T target)
        {
            return new DataPair<T>(source, target);
        }

        public String source;
        public T target;

        private DataPair(String source, T target)
        {
            this.source = source;
            this.target = target;
        }
    }
}
