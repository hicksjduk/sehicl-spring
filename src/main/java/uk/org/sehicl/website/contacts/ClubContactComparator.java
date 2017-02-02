package uk.org.sehicl.website.contacts;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClubContactComparator implements Comparator<ContactDetails>
{
    public static final List<String> ROLES = Arrays.asList("Secretary", "Captain", "Senior",
            "A team", "B team", "C team", "Under-16", "Under-13");

    @Override
    public int compare(ContactDetails o1, ContactDetails o2)
    {
        int answer = o1.getRole().getClub().compareTo(o2.getRole().getClub());
        if (answer == 0)
        {
            answer = ROLES.indexOf(o1.getRole().getName()) - ROLES.indexOf(o2.getRole().getName());
        }
        return answer;
    }
}
