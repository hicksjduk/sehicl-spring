package uk.org.sehicl.website.resultentry;

import java.util.function.UnaryOperator;

public enum MatchDataField
{
    TEAM_BATTING_FIRST("battingfirst");
    
    private final String fieldName;
    
    private MatchDataField(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public String getValue(UnaryOperator<String> valueGetter)
    {
        return valueGetter.apply(fieldName);
    }
}
