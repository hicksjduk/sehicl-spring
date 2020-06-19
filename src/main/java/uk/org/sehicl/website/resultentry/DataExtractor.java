package uk.org.sehicl.website.resultentry;

import java.util.function.UnaryOperator;

import org.apache.commons.lang3.StringUtils;

import uk.org.sehicl.website.resultentry.MatchData.BattingData;
import uk.org.sehicl.website.resultentry.MatchData.BowlingData;
import uk.org.sehicl.website.resultentry.MatchData.InningsData;
import uk.org.sehicl.website.rules.Rules;

public class DataExtractor
{
    private final UnaryOperator<String> fieldExtractor;
    private final Rules rules;

    public DataExtractor(UnaryOperator<String> fieldExtractor, Rules rules)
    {
        this.fieldExtractor = fieldExtractor;
        this.rules = rules;
    }

    public boolean homeTeamBattingFirst()
    {
        String value = MatchDataField.TEAM_BATTING_FIRST.getValue(fieldExtractor);
        return StringUtils.isBlank(value) || value.startsWith("home");
    }

    public InningsData getInnings(int sequence)
    {
        Integer extras = InningsDataField.EXTRAS.getInt(fieldExtractor, sequence);
        Integer total = InningsDataField.TOTAL.getInt(fieldExtractor, sequence);
        Integer wickets = InningsDataField.WICKETS.getInt(fieldExtractor, sequence);
        Integer balls = InningsDataField.OVERS.get(fieldExtractor, rules::oversToBalls, sequence);
        if (extras != null || total != null || wickets != null || balls != null)
        {
            boolean homeTeamBatting = sequence == (homeTeamBattingFirst() ? 1 : 2);
            return new InningsData(homeTeamBatting, extras, total, wickets, balls);
        }
        return null;
    }

    public BattingData getBatting(int innings, int sequence)
    {
        String batsman = nullIfBlank(PlayerDataField.BATSMAN.get(fieldExtractor, innings, sequence));
        HowOut howOut = PlayerDataField.HOW_OUT
                .get(fieldExtractor, HowOut::fromString, innings, sequence);
        Integer runs = PlayerDataField.RUNS_SCORED.getInt(fieldExtractor, innings, sequence);
        String bowler = nullIfBlank(PlayerDataField.WICKET_BOWLER.get(fieldExtractor, innings, sequence));
        if (batsman != null || (howOut != null && howOut != HowOut.DID_NOT_BAT)
                || runs != null || bowler != null)
            return new BattingData(batsman, howOut, bowler, runs);
        return null;
    }
    
    private String nullIfBlank(String str)
    {
        if (StringUtils.isBlank(str))
            return null;
        return str;
    }

    public BowlingData getBowling(int innings, int sequence)
    {
        String bowler = PlayerDataField.BOWLER.get(fieldExtractor, innings, sequence);
        Integer balls = PlayerDataField.OVERS
                .get(fieldExtractor, rules::oversToBalls, innings, sequence);
        Integer runs = PlayerDataField.RUNS_CONCEDED.getInt(fieldExtractor, innings, sequence);
        Integer wickets = PlayerDataField.WICKETS.getInt(fieldExtractor, innings, sequence);
        if (bowler != null || (balls != null && balls > 0) || runs != null || wickets != null)
            return new BowlingData(bowler, balls, runs, wickets);
        return null;
    }
}
