package uk.org.sehicl.website.resultentry;

import java.util.function.UnaryOperator;

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

    public String getValue(UnaryOperator<String> valueGetter, int innings, int sequence)
    {
        return valueGetter.apply(getFieldName(innings, sequence));
    }

    public String getFieldName(int innings, int sequence)
    {
        return String.format("%s%d%d", this, innings, sequence);
    }
}
