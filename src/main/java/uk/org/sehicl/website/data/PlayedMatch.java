package uk.org.sehicl.website.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import uk.org.sehicl.website.rules.Rules;

@JsonPropertyOrder(value =
{ "overLimit", "submittedByEmail", "teamInMatch" })
public class PlayedMatch implements Outcome
{
    private boolean submittedByEmail;
    private final List<TeamInMatch> teams = new ArrayList<>();
    private Integer overLimit;

    @JacksonXmlProperty(localName = "submittedByEmail")
    @JsonInclude(Include.NON_NULL)
    private String getSubmittedByEmail()
    {
        return submittedByEmail ? "" : null;
    }

    public boolean isSubmittedByEmail()
    {
        return submittedByEmail;
    }

    public void setSubmittedByEmail(String submittedByEmail)
    {
        this.submittedByEmail = true;
    }

    @JacksonXmlProperty(localName = "teamInMatch")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<TeamInMatch> getTeams()
    {
        return teams;
    }

    @JacksonXmlProperty(localName = "teamInMatch")
    public void addTeam(TeamInMatch team)
    {
        teams.add(team);
    }

    @JsonInclude(Include.NON_NULL)
    public Integer getOverLimit()
    {
        return overLimit;
    }

    public void setOverLimit(Integer overLimit)
    {
        this.overLimit = overLimit;
    }

    @Override
    public boolean isComplete(Rules rules)
    {
        boolean answer = false;
        if (teams.size() == 2 && teams.stream().filter(TeamInMatch::isBattingFirst).count() == 1)
        {
            int maxBalls = (overLimit == null ? rules.getOversPerInnings() : overLimit)
                    * rules.getBallsPerOver();
            int maxWickets = rules.getMaxWickets();
            final List<Innings> innings = teams
                    .stream()
                    .sorted((t1, t2) -> Boolean.compare(t2.isBattingFirst(), t1.isBattingFirst()))
                    .map(TeamInMatch::getInnings)
                    .filter(Objects::nonNull).collect(Collectors.toList());
            if (innings.stream().count() == 2)
            {
                final Iterator<Innings> iterator = innings.iterator();
                Innings firstInnings = iterator.next();
                Innings secondInnings = iterator.next();
                answer = firstInnings.isComplete(maxWickets, maxBalls) && 
                (secondInnings.isComplete(maxWickets, maxBalls) ||
                        (secondInnings.getRunsScored() > firstInnings.getRunsScored() &&
                                secondInnings.getWicketsLost() != null && secondInnings.getWicketsLost() < rules.getMaxWickets()));
            }
        }
        return answer;
    }
}
