package uk.org.sehicl.website.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import uk.org.sehicl.website.rules.Rules;

@JsonPropertyOrder(value = { "overLimit", "submittedByEmail", "teamInMatch" })
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
    public Completeness getCompleteness(Rules rules)
    {
        var teamsByBatting1st = teams
                .stream()
                .collect(Collectors.partitioningBy(TeamInMatch::isBattingFirst));
        var firstInns = Optional
                .ofNullable(teamsByBatting1st.get(true))
                .filter(c -> c.size() == 1)
                .map(c -> c.get(0))
                .map(TeamInMatch::getInnings);
        var secondInns = Optional
                .ofNullable(teamsByBatting1st.get(false))
                .filter(c -> c.size() == 1)
                .map(c -> c.get(0))
                .map(TeamInMatch::getInnings);
        if (Stream.of(firstInns, secondInns).anyMatch(Optional::isEmpty))
            return Completeness.INCOMPLETE;
        var maxBalls = Optional.ofNullable(overLimit).orElse(rules.getOversPerInnings())
                * rules.getBallsPerOver();
        var maxWickets = rules.getMaxWickets();
        var bat2ndWon = firstInns.get().getRunsScored() < secondInns.get().getRunsScored();
        var winningSecondInns = secondInns.filter(i -> bat2ndWon);
        Function<Innings, Completeness> oversCheck = i ->
        {
            var balls = i.getBallsBowled(maxBalls);
            if (balls > maxBalls)
                return Completeness.INCOMPLETE;
            if (i.isAllOut(maxWickets))
                return Completeness.CONSISTENT;
            if (winningSecondInns.orElse(null) == i)
                return Completeness.CONSISTENT;
            if (balls < maxBalls)
                return Completeness.INCOMPLETE;
            return Completeness.CONSISTENT;
        };
        Function<Innings, Completeness> wicketsCheck = i ->
        {
            if (winningSecondInns.orElse(null) == i)
                if (i.getWicketsLost(maxWickets) == maxWickets)
                    return Completeness.INCOMPLETE;
            return Completeness.CONSISTENT;
        };
        return Stream
                .of(firstInns, secondInns)
                .map(Optional::get)
                .flatMap(i -> Stream.of(oversCheck, wicketsCheck).map(f -> f.apply(i)))
                .sorted()
                .findFirst()
                .get();
    }

    @Override
    public String toString()
    {
        return "PlayedMatch [submittedByEmail=" + submittedByEmail + ", teams=" + teams
                + ", overLimit=" + overLimit + "]";
    }
}
