package uk.org.sehicl.website.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import uk.org.sehicl.website.rules.Rules;

@JsonPropertyOrder(value =
{ "reason", "winner" })
public class AwardedMatch implements Outcome
{
    private String reason;
    private String winnerId;

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    @JsonIgnore
    public String getWinnerId()
    {
        return winnerId;
    }

    @JacksonXmlProperty(localName = "winner")
    private TeamReference getWinnerRef()
    {
        TeamReference answer = new TeamReference();
        answer.setId(winnerId);
        return answer;
    }

    public void setWinner(TeamReference teamRef)
    {
        winnerId = teamRef == null ? null : teamRef.getId();
    }

    @Override
    public boolean isComplete(Rules rules)
    {
        return winnerId != null;
    }
}
