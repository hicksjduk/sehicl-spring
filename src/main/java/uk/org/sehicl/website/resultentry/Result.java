package uk.org.sehicl.website.resultentry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import uk.org.sehicl.website.data.League;
import uk.org.sehicl.website.data.Team;
import uk.org.sehicl.website.report.ModelAndRules;
import uk.org.sehicl.website.resultentry.MatchData.ResultException;
import uk.org.sehicl.website.rules.Rules;

public class Result
{
    private final MatchData match;
    private final InningsData[] innings;
    private final Rules rules;
    private final Map<String, List<String>> validationErrors = new HashMap<>();

    public Result(ModelAndRules modelAndRules, String leagueId, String homeTeamId,
            String awayTeamId, UnaryOperator<String> dataGetter) throws ResultException
    {
        this.rules = modelAndRules.rules;
        this.match = new MatchData(modelAndRules.model, leagueId, homeTeamId, awayTeamId);
        boolean homeBattingFirst = dataGetter.apply("battingFirst").equals("hometeam");
        League league = modelAndRules.model.getLeague(leagueId);
        innings = Stream
                .of(new InningsData(dataGetter, homeBattingFirst ? 1 : 2,
                        league.getTeam(homeTeamId), league.getTeam(awayTeamId)),
                        new InningsData(dataGetter, homeBattingFirst ? 2 : 1,
                                league.getTeam(awayTeamId), league.getTeam(homeTeamId)))
                .sorted(Comparator.comparing((InningsData id) -> id.sequence))
                .toArray(InningsData[]::new);
    }

    public void validate()
    {
        int maxBallsPerInnings = rules.getOversPerInnings() * rules.getBallsPerOver();
        IntStream.rangeClosed(0, 1).forEach(i -> validate(innings[i], rules, i + 1));
        if (innings[0].wickets < rules.getMaxWickets() && innings[0].balls != maxBallsPerInnings)
            addError(InningsDataField.OVERS.getFieldName(1),
                    "First innings must be %d overs if the batting side is not all out",
                    rules.getOversPerInnings());
        if (innings[1].wickets < rules.getMaxWickets() && innings[1].balls != maxBallsPerInnings
                && innings[1].total <= innings[0].total)
            addError(InningsDataField.OVERS.getFieldName(2),
                    "Second innings must be %d overs if the batting side is not all out "
                            + "and does not win",
                    rules.getOversPerInnings());
    }

    private void validate(InningsData inns, Rules rules, int inningsNumber)
    {
        if (inns.extras.isEmpty())
            addError(InningsDataField.EXTRAS.getFieldName(inningsNumber), "Extras must be entered");
        if (inns.balls > rules.getOversPerInnings() * rules.getBallsPerOver())
            addError(InningsDataField.OVERS.getFieldName(inningsNumber),
                    "Innings cannot be more than %d overs", rules.getOversPerInnings());
        int runsConcededByBowlers = inns.bowlers
                .stream()
                .map(b -> b.runsConceded)
                .filter(OptionalInt::isPresent)
                .mapToInt(OptionalInt::getAsInt)
                .sum();
        if (runsConcededByBowlers > inns.total)
            addError(InningsDataField.TOTAL.getFieldName(inningsNumber),
                    "Innings total cannot be less than the total number of runs conceded by the bowlers (%d)",
                    runsConcededByBowlers);
        List<String> bowlerNames = inns.bowlers
                .stream()
                .map(b -> b.name)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        IntStream
                .range(0, inns.batsmen.size())
                .forEach(i -> validate(inns.batsmen.get(i), inningsNumber, i + 1, bowlerNames));
        IntStream
                .range(0, inns.bowlers.size())
                .forEach(i -> validate(inns.bowlers.get(i), inningsNumber, i + 1));
    }

    private void validate(BatsmanData batsman, int inningsNumber, int batsmanNumber,
            List<String> bowlerNames)
    {
        if (StringUtils.isBlank(batsman.name))
            addError(PlayerDataField.BATSMAN.getFieldName(inningsNumber, batsmanNumber),
                    "Batsman's name must be entered");
        if (batsman.howOut.creditedToBowler())
        {
            if (StringUtils.isBlank(batsman.bowler))
                addError(PlayerDataField.WICKET_BOWLER.getFieldName(inningsNumber, batsmanNumber),
                        "Bowler's name must be entered for dismissal type: %s",
                        batsman.howOut.getText());
            else if (!bowlerNames.contains(batsman.bowler))
                addError(PlayerDataField.WICKET_BOWLER.getFieldName(inningsNumber, batsmanNumber),
                        "Bowler must be one of those who bowled");
        }
        if (batsman.howOut.canHaveScore() && batsman.runsScored.isEmpty())
            addError(PlayerDataField.RUNS_SCORED.getFieldName(inningsNumber, batsmanNumber),
                    "Batsman batted so a score must be entered");
    }

    private void validate(BowlerData bowler, int inningsNumber, int bowlerNumber)
    {
        if (StringUtils.isBlank(bowler.name))
            addError(PlayerDataField.BOWLER.getFieldName(inningsNumber, bowlerNumber),
                    "Bowler's name must be entered");
        if (bowler.runsConceded.isEmpty())
            addError(PlayerDataField.RUNS_CONCEDED.getFieldName(inningsNumber, bowlerNumber),
                    "Runs conceded must be entered");
    }

    private void addError(String field, String message, Object... messageParameters)
    {
        validationErrors
                .computeIfAbsent(field, f -> new ArrayList<>())
                .add(String.format(message, messageParameters));
    }

    public static class InningsData
    {
        private final int sequence;
        private final Team battingTeam;
        private final Team bowlingTeam;
        private final OptionalInt extras;
        private final int total;
        private final int wickets;
        private final int balls;
        private final List<BatsmanData> batsmen;
        private final List<BowlerData> bowlers;

        public InningsData(UnaryOperator<String> dataGetter, int sequence, Team battingTeam,
                Team bowlingTeam)
        {
            this.sequence = sequence;
            this.battingTeam = battingTeam;
            this.bowlingTeam = bowlingTeam;
            batsmen = IntStream
                    .rangeClosed(1, 6)
                    .mapToObj(seq -> new BatsmanData(dataGetter, sequence, seq))
                    .filter(d -> StringUtils.isNotBlank(d.name) || d.runsScored.isPresent()
                            || StringUtils.isNotBlank(d.bowler))
                    .collect(Collectors.toList());
            bowlers = IntStream
                    .rangeClosed(1, 6)
                    .mapToObj(seq -> new BowlerData(dataGetter, sequence, seq))
                    .filter(d -> StringUtils.isNotBlank(d.name) || d.balls != 0
                            || d.runsConceded.isPresent())
                    .collect(Collectors.toList());
            extras = Stream
                    .of(InningsDataField.EXTRAS.getValue(dataGetter, sequence))
                    .filter(StringUtils::isNotBlank)
                    .map(String::strip)
                    .mapToInt(Integer::parseInt)
                    .findFirst();
            total = Stream
                    .concat(Stream.of(extras), batsmen.stream().map(b -> b.runsScored))
                    .filter(OptionalInt::isPresent)
                    .mapToInt(OptionalInt::getAsInt)
                    .sum();
            wickets = batsmen
                    .stream()
                    .map(b -> b.howOut)
                    .filter(Objects::nonNull)
                    .mapToInt(ho -> ho.isOut() ? 1 : 0)
                    .sum();
            balls = bowlers.stream().mapToInt(b -> b.balls).sum();
        }
    }

    public static class BatsmanData
    {
        private final String name;
        private final HowOut howOut;
        private final String bowler;
        private final OptionalInt runsScored;

        public BatsmanData(UnaryOperator<String> dataGetter, int innings, int sequence)
        {
            name = PlayerDataField.BATSMAN.getValue(dataGetter, innings, sequence).strip();
            howOut = HowOut
                    .valueOf(PlayerDataField.HOW_OUT.getValue(dataGetter, innings, sequence));
            bowler = PlayerDataField.WICKET_BOWLER.getValue(dataGetter, innings, sequence).strip();
            runsScored = Stream
                    .of(PlayerDataField.RUNS_SCORED.getValue(dataGetter, innings, sequence))
                    .filter(StringUtils::isNotBlank)
                    .map(String::strip)
                    .mapToInt(Integer::parseInt)
                    .findFirst();
        }
    }

    public static class BowlerData
    {
        private final String name;
        private final int balls;
        private final OptionalInt runsConceded;

        public BowlerData(UnaryOperator<String> dataGetter, int innings, int sequence)
        {
            name = PlayerDataField.BOWLER.getValue(dataGetter, innings, sequence);
            balls = Integer.parseInt(PlayerDataField.OVERS.getValue(dataGetter, innings, sequence));
            runsConceded = Stream
                    .of(PlayerDataField.RUNS_CONCEDED.getValue(dataGetter, innings, sequence))
                    .filter(StringUtils::isNotBlank)
                    .mapToInt(Integer::parseInt)
                    .findFirst();
        }
    }
}
