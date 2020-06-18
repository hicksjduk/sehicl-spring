package uk.org.sehicl.website.resultentry;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.apache.commons.lang3.StringUtils;

public enum PlayerDataField
{
    BATSMAN,
    HOW_OUT,
    WICKET_BOWLER,
    RUNS_SCORED,
    BOWLER,
    OVERS,
    RUNS_CONCEDED,
    WICKETS;

    public <T> T get(UnaryOperator<String> valueGetter, Function<String, T> converter, int innings,
            int sequence)
    {
        return converter.apply(valueGetter.apply(getFieldName(innings, sequence)));
    }

    public String get(UnaryOperator<String> valueGetter, int innings, int sequence)
    {
        return get(valueGetter, Function.identity(), innings, sequence);
    }

    public Integer getInt(UnaryOperator<String> valueGetter, int innings, int sequence)
    {
        return get(valueGetter, v -> StringUtils.isEmpty(v) ? null : Integer.parseInt(v), innings,
                sequence);
    }

    public String getFieldName(int innings, int sequence)
    {
        return String.format("%s%d%d", this, innings, sequence);
    }
}
