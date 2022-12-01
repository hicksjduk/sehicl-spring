package uk.org.sehicl.website.contacts;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public interface ClubContacts
{
    public static final List<String> ROLES = Arrays
            .asList("Secretary", "Captain", "Senior", "A team", "B team", "C team", "Under-16",
                    "Under-13");

    public static int roleIndex(String str)
    {
        return IntStream
                .range(0, ROLES.size())
                .filter(i -> str.startsWith(ROLES.get(i)))
                .findFirst()
                .orElse(-1);
    }

    public static final Comparator<ContactDetails> COMPARATOR = Comparator
            .comparing(ContactDetails::getRole,
                    Comparator
                            .comparing(Role::getClub)
                            .thenComparing(Role::getName,
                                    Comparator
                                            .comparingInt(ClubContacts::roleIndex)
                                            .thenComparing(Comparator.naturalOrder())))
            .thenComparing(ContactDetails::getName);
}
