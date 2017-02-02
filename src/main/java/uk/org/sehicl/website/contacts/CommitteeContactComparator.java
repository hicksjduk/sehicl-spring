package uk.org.sehicl.website.contacts;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CommitteeContactComparator implements Comparator<ContactDetails>
{
    public static final List<String> ROLES = Arrays.asList("President", "Chairman",
            "Vice-Chairman", "Secretary", "Treasurer", "Umpires' Co-ordinator",
            "Fixture Secretary", "Webmaster");

    @Override
    public int compare(ContactDetails o1, ContactDetails o2)
    {
        return ROLES.indexOf(o1.getRole().getName()) - ROLES.indexOf(o2.getRole().getName());
    }
}
