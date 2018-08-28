package uk.org.sehicl.website.contacts;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public interface ClubContacts
{
    public static final List<String> ROLES = Arrays.asList("Secretary", "Captain", "Senior",
            "A team", "B team", "C team", "Under-16", "Under-13");

    public static final Comparator<ContactDetails> COMPARATOR = Comparator
            .comparing(ContactDetails::getRole, Comparator
                    .comparing(Role::getClub)
                    .thenComparingInt(r -> ROLES.indexOf(r.getName())))
            .thenComparing(ContactDetails::getName);
}
