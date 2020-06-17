package uk.org.sehicl.website.resultentry;

import java.util.function.UnaryOperator;

public enum InningsDataField
{
    EXTRAS, TOTAL, WICKETS, OVERS;
    
    public String getValue(UnaryOperator<String> valueGetter, int sequence)
    {
        return valueGetter.apply(getFieldName(sequence));
    }

    public String getFieldName(int sequence)
    {
        return String.format("%s%d", this, sequence);
    }
}
