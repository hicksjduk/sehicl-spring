package uk.org.sehicl.website.resultentry;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.Mockito.*;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.org.sehicl.website.resultentry.MatchData.InningsData;
import uk.org.sehicl.website.rules.Rules;

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
        return Stream
                .of(arguments(1, "hometeam", "", "", "", "", null),
                        arguments(1, "hometeam", "4", "", "", "",
                                new InningsData(true, 4, null, null, null)),
                        arguments(1, "hometeam", "", "", "", "", null),
                        arguments(1, "hometeam", "4", "", "", "",
                                new InningsData(true, 4, null, null, null)),
                        arguments(1, "hometeam", "", "15", "", "",
                                new InningsData(true, null, 15, null, null)),
                        arguments(1, "hometeam", "4", "15", "", "",
                                new InningsData(true, 4, 15, null, null)),
                        arguments(1, "hometeam", "", "", "2", "",
                                new InningsData(true, null, null, 2, null)),
                        arguments(1, "hometeam", "4", "", "2", "",
                                new InningsData(true, 4, null, 2, null)),
                        arguments(1, "hometeam", "", "", "2", "",
                                new InningsData(true, null, null, 2, null)),
                        arguments(1, "hometeam", "4", "", "2", "",
                                new InningsData(true, 4, null, 2, null)),
                        arguments(1, "hometeam", "", "15", "2", "",
                                new InningsData(true, null, 15, 2, null)),
                        arguments(1, "hometeam", "4", "15", "2", "",
                                new InningsData(true, 4, 15, 2, null)),
                        arguments(1, "hometeam", "", "", "", "11.2",
                                new InningsData(true, null, null, null, 68)),
                        arguments(1, "hometeam", "4", "", "", "11.2",
                                new InningsData(true, 4, null, null, 68)),
                        arguments(1, "hometeam", "", "", "", "11.2",
                                new InningsData(true, null, null, null, 68)),
                        arguments(1, "hometeam", "4", "", "", "11.2",
                                new InningsData(true, 4, null, null, 68)),
                        arguments(1, "hometeam", "", "15", "", "11.2",
                                new InningsData(true, null, 15, null, 68)),
                        arguments(1, "hometeam", "4", "15", "", "11.2",
                                new InningsData(true, 4, 15, null, 68)),
                        arguments(1, "hometeam", "", "", "2", "11.2",
                                new InningsData(true, null, null, 2, 68)),
                        arguments(1, "hometeam", "4", "", "2", "11.2",
                                new InningsData(true, 4, null, 2, 68)),
                        arguments(1, "hometeam", "", "", "2", "11.2",
                                new InningsData(true, null, null, 2, 68)),
                        arguments(1, "hometeam", "4", "", "2", "11.2",
                                new InningsData(true, 4, null, 2, 68)),
                        arguments(1, "hometeam", "", "15", "2", "11.2",
                                new InningsData(true, null, 15, 2, 68)),
                        arguments(1, "hometeam", "4", "15", "2", "11.2",
                                new InningsData(true, 4, 15, 2, 68)),
                        arguments(2, "hometeam", "", "", "", "", null),
                        arguments(2, "hometeam", "4", "", "", "",
                                new InningsData(false, 4, null, null, null)),
                        arguments(2, "hometeam", "", "", "", "", null),
                        arguments(2, "hometeam", "4", "", "", "",
                                new InningsData(false, 4, null, null, null)),
                        arguments(2, "hometeam", "", "15", "", "",
                                new InningsData(false, null, 15, null, null)),
                        arguments(2, "hometeam", "4", "15", "", "",
                                new InningsData(false, 4, 15, null, null)),
                        arguments(2, "hometeam", "", "", "2", "",
                                new InningsData(false, null, null, 2, null)),
                        arguments(2, "hometeam", "4", "", "2", "",
                                new InningsData(false, 4, null, 2, null)),
                        arguments(2, "hometeam", "", "", "2", "",
                                new InningsData(false, null, null, 2, null)),
                        arguments(2, "hometeam", "4", "", "2", "",
                                new InningsData(false, 4, null, 2, null)),
                        arguments(2, "hometeam", "", "15", "2", "",
                                new InningsData(false, null, 15, 2, null)),
                        arguments(2, "hometeam", "4", "15", "2", "",
                                new InningsData(false, 4, 15, 2, null)),
                        arguments(2, "hometeam", "", "", "", "11.2",
                                new InningsData(false, null, null, null, 68)),
                        arguments(2, "hometeam", "4", "", "", "11.2",
                                new InningsData(false, 4, null, null, 68)),
                        arguments(2, "hometeam", "", "", "", "11.2",
                                new InningsData(false, null, null, null, 68)),
                        arguments(2, "hometeam", "4", "", "", "11.2",
                                new InningsData(false, 4, null, null, 68)),
                        arguments(2, "hometeam", "", "15", "", "11.2",
                                new InningsData(false, null, 15, null, 68)),
                        arguments(2, "hometeam", "4", "15", "", "11.2",
                                new InningsData(false, 4, 15, null, 68)),
                        arguments(2, "hometeam", "", "", "2", "11.2",
                                new InningsData(false, null, null, 2, 68)),
                        arguments(2, "hometeam", "4", "", "2", "11.2",
                                new InningsData(false, 4, null, 2, 68)),
                        arguments(2, "hometeam", "", "", "2", "11.2",
                                new InningsData(false, null, null, 2, 68)),
                        arguments(2, "hometeam", "4", "", "2", "11.2",
                                new InningsData(false, 4, null, 2, 68)),
                        arguments(2, "hometeam", "", "15", "2", "11.2",
                                new InningsData(false, null, 15, 2, 68)),
                        arguments(2, "hometeam", "4", "15", "2", "11.2",
                                new InningsData(false, 4, 15, 2, 68)),
                        arguments(1, "awayteam", "", "", "", "", null),
                        arguments(1, "awayteam", "4", "", "", "",
                                new InningsData(false, 4, null, null, null)),
                        arguments(1, "awayteam", "", "", "", "", null),
                        arguments(1, "awayteam", "4", "", "", "",
                                new InningsData(false, 4, null, null, null)),
                        arguments(1, "awayteam", "", "15", "", "",
                                new InningsData(false, null, 15, null, null)),
                        arguments(1, "awayteam", "4", "15", "", "",
                                new InningsData(false, 4, 15, null, null)),
                        arguments(1, "awayteam", "", "", "2", "",
                                new InningsData(false, null, null, 2, null)),
                        arguments(1, "awayteam", "4", "", "2", "",
                                new InningsData(false, 4, null, 2, null)),
                        arguments(1, "awayteam", "", "", "2", "",
                                new InningsData(false, null, null, 2, null)),
                        arguments(1, "awayteam", "4", "", "2", "",
                                new InningsData(false, 4, null, 2, null)),
                        arguments(1, "awayteam", "", "15", "2", "",
                                new InningsData(false, null, 15, 2, null)),
                        arguments(1, "awayteam", "4", "15", "2", "",
                                new InningsData(false, 4, 15, 2, null)),
                        arguments(1, "awayteam", "", "", "", "11.2",
                                new InningsData(false, null, null, null, 68)),
                        arguments(1, "awayteam", "4", "", "", "11.2",
                                new InningsData(false, 4, null, null, 68)),
                        arguments(1, "awayteam", "", "", "", "11.2",
                                new InningsData(false, null, null, null, 68)),
                        arguments(1, "awayteam", "4", "", "", "11.2",
                                new InningsData(false, 4, null, null, 68)),
                        arguments(1, "awayteam", "", "15", "", "11.2",
                                new InningsData(false, null, 15, null, 68)),
                        arguments(1, "awayteam", "4", "15", "", "11.2",
                                new InningsData(false, 4, 15, null, 68)),
                        arguments(1, "awayteam", "", "", "2", "11.2",
                                new InningsData(false, null, null, 2, 68)),
                        arguments(1, "awayteam", "4", "", "2", "11.2",
                                new InningsData(false, 4, null, 2, 68)),
                        arguments(1, "awayteam", "", "", "2", "11.2",
                                new InningsData(false, null, null, 2, 68)),
                        arguments(1, "awayteam", "4", "", "2", "11.2",
                                new InningsData(false, 4, null, 2, 68)),
                        arguments(1, "awayteam", "", "15", "2", "11.2",
                                new InningsData(false, null, 15, 2, 68)),
                        arguments(1, "awayteam", "4", "15", "2", "11.2",
                                new InningsData(false, 4, 15, 2, 68)),
                        arguments(2, "awayteam", "", "", "", "", null),
                        arguments(2, "awayteam", "4", "", "", "",
                                new InningsData(true, 4, null, null, null)),
                        arguments(2, "awayteam", "", "", "", "", null),
                        arguments(2, "awayteam", "4", "", "", "",
                                new InningsData(true, 4, null, null, null)),
                        arguments(2, "awayteam", "", "15", "", "",
                                new InningsData(true, null, 15, null, null)),
                        arguments(2, "awayteam", "4", "15", "", "",
                                new InningsData(true, 4, 15, null, null)),
                        arguments(2, "awayteam", "", "", "2", "",
                                new InningsData(true, null, null, 2, null)),
                        arguments(2, "awayteam", "4", "", "2", "",
                                new InningsData(true, 4, null, 2, null)),
                        arguments(2, "awayteam", "", "", "2", "",
                                new InningsData(true, null, null, 2, null)),
                        arguments(2, "awayteam", "4", "", "2", "",
                                new InningsData(true, 4, null, 2, null)),
                        arguments(2, "awayteam", "", "15", "2", "",
                                new InningsData(true, null, 15, 2, null)),
                        arguments(2, "awayteam", "4", "15", "2", "",
                                new InningsData(true, 4, 15, 2, null)),
                        arguments(2, "awayteam", "", "", "", "11.2",
                                new InningsData(true, null, null, null, 68)),
                        arguments(2, "awayteam", "4", "", "", "11.2",
                                new InningsData(true, 4, null, null, 68)),
                        arguments(2, "awayteam", "", "", "", "11.2",
                                new InningsData(true, null, null, null, 68)),
                        arguments(2, "awayteam", "4", "", "", "11.2",
                                new InningsData(true, 4, null, null, 68)),
                        arguments(2, "awayteam", "", "15", "", "11.2",
                                new InningsData(true, null, 15, null, 68)),
                        arguments(2, "awayteam", "4", "15", "", "11.2",
                                new InningsData(true, 4, 15, null, 68)),
                        arguments(2, "awayteam", "", "", "2", "11.2",
                                new InningsData(true, null, null, 2, 68)),
                        arguments(2, "awayteam", "4", "", "2", "11.2",
                                new InningsData(true, 4, null, 2, 68)),
                        arguments(2, "awayteam", "", "", "2", "11.2",
                                new InningsData(true, null, null, 2, 68)),
                        arguments(2, "awayteam", "4", "", "2", "11.2",
                                new InningsData(true, 4, null, 2, 68)),
                        arguments(2, "awayteam", "", "15", "2", "11.2",
                                new InningsData(true, null, 15, 2, 68)),
                        arguments(2, "awayteam", "4", "15", "2", "11.2",
                                new InningsData(true, 4, 15, 2, 68)));
    }
}
