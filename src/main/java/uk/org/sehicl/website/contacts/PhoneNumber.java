package uk.org.sehicl.website.contacts;

import java.util.Arrays;
import java.util.List;

public class PhoneNumber
{
    private final String number;

    public PhoneNumber(String number)
    {
        this.number = number.replaceAll("[^\\d]", "");
    }
    
    public String toString()
    {
        StringBuffer answer = new StringBuffer(number);
        List<Integer> splitPoints = null;
        if (number.startsWith("02"))
        {
            splitPoints = Arrays.asList(3, 7);
        }
        else if (number.startsWith("011") || number.matches("01\\d1.*"))
        {
            splitPoints = Arrays.asList(4, 7);
        }
        else
        {
            splitPoints = Arrays.asList(5);
        }
        int splitIndex = 0;
        for (int split : splitPoints)
        {
            int splitPoint = split + splitIndex++;
            if (splitPoint < answer.length())
            {
                answer.insert(splitPoint, " ");
            }
        }
        return answer.toString();
    }
}
