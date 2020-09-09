package uk.org.sehicl.website.resultentry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public enum HowOut
{
    DID_NOT_BAT("Did not bat"),
    NOT_OUT("Not out", Flag.CAN_HAVE_SCORE),
    RETIRED("Retired", Flag.CAN_HAVE_SCORE),
    RUN_OUT("Run out", Flag.CAN_HAVE_SCORE, Flag.IS_OUT),
    BOWLED("Bowled", Flag.CAN_HAVE_SCORE, Flag.IS_OUT, Flag.CREDITED_TO_BOWLER),
    CAUGHT("Caught", Flag.CAN_HAVE_SCORE, Flag.IS_OUT, Flag.CREDITED_TO_BOWLER),
    STUMPED("Stumped", Flag.CAN_HAVE_SCORE, Flag.IS_OUT, Flag.CREDITED_TO_BOWLER),
    LBW("LBW", Flag.CAN_HAVE_SCORE, Flag.IS_OUT, Flag.CREDITED_TO_BOWLER),
    ABSENT_NOT_OUT("Absent"),
    ABSENT_OUT("Absent out", Flag.IS_OUT),
    RETIRED_HURT_NOT_OUT("Retired hurt-not out", Flag.CAN_HAVE_SCORE),
    RETIRED_HURT_OUT("Retired hurt-out", Flag.CAN_HAVE_SCORE, Flag.IS_OUT),
    HIT_WICKET("Hit wicket", Flag.CAN_HAVE_SCORE, Flag.IS_OUT, Flag.CREDITED_TO_BOWLER),
    HIT_BALL_TWICE("Hit ball twice", Flag.CAN_HAVE_SCORE, Flag.IS_OUT),
    OBSTRUCTING_FIELD("Obstructing the field", Flag.CAN_HAVE_SCORE, Flag.IS_OUT),
    TIMED_OUT("Timed out", Flag.IS_OUT);

    private static String SEPARATOR = ";";
    
    private String text;
    private List<Flag> flags;

    public static HowOut fromString(String str)
    {
        if (StringUtils.isBlank(str))
            return null;
        return HowOut.valueOf(str.split(SEPARATOR)[0]);
    }

    private HowOut(String text, Flag... flags)
    {
        this.text = text;
        this.flags = Arrays.asList(flags);
    }

    public String getText()
    {
        return text;
    }

    public boolean canHaveScore()
    {
        return flags.contains(Flag.CAN_HAVE_SCORE);
    }

    public boolean isOut()
    {
        return flags.contains(Flag.IS_OUT);
    }

    public boolean creditedToBowler()
    {
        return flags.contains(Flag.CREDITED_TO_BOWLER);
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getAttribute()
    {
        return Stream
                .concat(Stream.of(this.toString()), flags.stream().map(f -> f.value))
                .collect(Collectors.joining(SEPARATOR));
    }

    private static enum Flag
    {
        CAN_HAVE_SCORE("score"),
        IS_OUT("out"),
        CREDITED_TO_BOWLER("bowler");

        private final String value;

        private Flag(String value)
        {
            this.value = value;
        }
    }
}
