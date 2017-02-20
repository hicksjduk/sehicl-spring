package uk.org.sehicl.website.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder(value={"runs", "out", "notes"})
public class Batsman extends Performance implements Comparable<Batsman>
{
    private int runsScored;
    private boolean out;

    @JacksonXmlProperty(localName = "runs")
    public int getRunsScored()
    {
        return runsScored;
    }

    @JacksonXmlProperty(localName = "runs")
    public void setRunsScored(int runsScored)
    {
        this.runsScored = runsScored;
    }

    public boolean isOut()
    {
        return out;
    }

    public void setOut(boolean out)
    {
        this.out = out;
    }

    @Override
    public int compareTo(Batsman o)
    {
        int answer = o.runsScored - runsScored;
        if (answer == 0)
        {
            answer = (out ? 1 : 0) - (o.out ? 1 : 0);
        }
        return answer;
    }
}