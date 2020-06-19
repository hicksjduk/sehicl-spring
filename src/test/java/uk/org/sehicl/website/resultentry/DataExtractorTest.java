package uk.org.sehicl.website.resultentry;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.Mockito.*;

import java.util.function.Function;
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
                .of(new DataPair(null, null), new DataPair("", null), new DataPair("4", 4))
                .and(new DataPair(null, null), new DataPair("", null), new DataPair("15", 15))
                .and(new DataPair(null, null), new DataPair("", null), new DataPair("2", 2))
                .and(new DataPair(null, null), new DataPair("", null), new DataPair("11.3", 69))
                .and(1, 2)
                .and("hometeam", "awayteam")
                .build()
                .map(DataExtractorTest::testGetInnings);
    }

    static Arguments testGetInnings(Combination comb)
    {
        DataPair extras = comb.next(DataPair.class);
        DataPair total = comb.next(DataPair.class);
        DataPair wickets = comb.next(DataPair.class);
        DataPair overs = comb.next(DataPair.class);
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
            assertThat(result.getBowler()).isEqualTo(expected.getBowler());
            assertThat(result.getRuns()).isEqualTo(expected.getRuns());
        }
    }
    
    static Stream<Arguments> testGetBatting()
    {
        return CartesianProductBuilder
                .of(1, 2)
                .and(1, 2, 3, 4, 5, 6)
                .and(null, "", "Jeremy")
                .and(null, "", "DID_NOT_BAT", "BOWLED")
                .and(new DataPair(null, null), new DataPair("", null), new DataPair("25", 25))
                .and(null, "", "Peter")
                .build()
                .map(DataExtractorTest::testGetBatting);
    }

    static Arguments testGetBatting(Combination comb)
    {
        int sequence = comb.nextInt();
        int innings = comb.nextInt();
        String batsman = comb.next(String.class);
        String howOut = comb.next(String.class);
        DataPair score = comb.next(DataPair.class);
        String bowler = comb.next(String.class);
        boolean dataFound = Stream
                .of(batsman, bowler, score.source, "DID_NOT_BAT".equals(howOut) ? null : howOut)
                .anyMatch(StringUtils::isNotBlank);
        BattingData expected = dataFound
                ? new BattingData(StringUtils.isBlank(batsman) ? null : batsman,
                        StringUtils.isBlank(howOut) ? null : HowOut.fromString(howOut),
                        StringUtils.isBlank(bowler) ? null : bowler, score.target)
                : null;
        return arguments(innings, sequence, batsman, howOut == null ? "" : howOut.toString(),
                score.source, bowler, expected);
    }
    
    private static class DataPair
    {
        public String source;
        public Integer target;

        public DataPair(String source, Integer target)
        {
            this.source = source;
            this.target = target;
        }
    }
}
