package uk.org.sehicl.website;

import java.text.DateFormat;
import java.text.DateFormat.Field;
import java.text.FieldPosition;
import java.util.Date;

/**
 * Date formatter that inserts the English ordinal suffix after the day of month component of a formatted date.
 */
public class OrdinalDateFormatter
{
    /**
     * The contained date formatter that actually does the formatting.
     */
    private final DateFormat formatter;

    /**
     * Initialises the object with the specified formatter.
     * 
     * @param formatter
     *            the formatter. May not be null.
     */
    public OrdinalDateFormatter(DateFormat formatter)
    {
        this.formatter = formatter;
    }

    /**
     * Formats the specified date.
     * 
     * @param date
     *            the date. May not be null.
     * @return the formatted date, with the appropriate suffix inserted if the day of month is present and numeric.
     */
    public String format(Date date)
    {
        StringBuffer sb = new StringBuffer();
        FieldPosition dateFieldPosition = new FieldPosition(Field.DAY_OF_MONTH);
        formatter.format(date, sb, dateFieldPosition);
        String dateFieldValue = sb.substring(dateFieldPosition.getBeginIndex(),
                dateFieldPosition.getEndIndex());
        sb.insert(dateFieldPosition.getEndIndex(), getOrdinalSuffix(dateFieldValue));
        return sb.toString();
    }

    /**
     * Gets the ordinal suffix that corresponds to the specified string, if it is numeric.
     * 
     * @param numStr
     *            the string. May not be null.
     * @return the relevant ordinal suffix (th, st, nd or rd) if the input value is numeric, otherwise an empty string.
     */
    private String getOrdinalSuffix(String numStr)
    {
        String answer = "";
        if (numStr.matches("\\d+"))
        {
            final String[] suffixes =
            { "th", "st", "nd", "rd" };
            int index = 0;
            int dayNum = Integer.parseInt(numStr);
            int units = dayNum % 10;
            if (units < 4)
            {
                int tens = dayNum / 10 % 10;
                if (tens != 1)
                {
                    index = units;
                }
            }
            answer = suffixes[index];
        }
        return answer;
    }
}
