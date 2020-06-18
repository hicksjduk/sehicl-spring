package uk.org.sehicl.website.resultentry;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.apache.commons.lang3.StringUtils;

public enum InningsDataField
{
    EXTRAS,
    TOTAL,
    WICKETS,
    OVERS;

    public <T> T get(UnaryOperator<String> valueGetter, Function<String, T> converter, int sequence)
    {
        return converter.apply(valueGetter.apply(getFieldName(sequence)));
    }

    public String get(UnaryOperator<String> valueGetter, int sequence)
    {
        return get(valueGetter, Function.identity(), sequence);
    }

    public Integer getInt(UnaryOperator<String> valueGetter, int sequence)
    {
        return get(valueGetter, v -> StringUtils.isEmpty(v) ? null : Integer.parseInt(v), sequence);
    }

    public String getFieldName(int sequence)
    {
        return String.format("%s%d", this, sequence);
    }
}
