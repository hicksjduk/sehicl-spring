package uk.org.sehicl.website.contacts;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public interface CommitteeContacts
{
    public static final List<String> ROLES = Arrays.asList("President", "Chairman", "Vice-Chairman",
            "Secretary", "Treasurer", "Umpires' Co-ordinator", "Fixture Secretary", "Webmaster",
            "Data Protection Officer");

    public static final Comparator<ContactDetails> COMPARATOR = Comparator.comparing(
            ContactDetails::getRole, Comparator.comparingInt(r -> ROLES.indexOf(r.getName())));
}
