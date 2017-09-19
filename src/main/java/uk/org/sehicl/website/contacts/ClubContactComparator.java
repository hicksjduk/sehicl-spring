package uk.org.sehicl.website.contacts;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ClubContactComparator implements Comparator<ContactDetails>
{
    public static final List<String> ROLES = Arrays.asList("Secretary", "Captain", "Senior",
            "A team", "B team", "C team", "Under-16", "Under-13");

    @Override
    public int compare(ContactDetails o1, ContactDetails o2)
    {
        int answer = compare(o1, o2, (o) -> o.getRole().getClub())
                .andThen(compare(o1, o2, (o) -> ROLES.indexOf(o.getRole().getName()))
                        .andThen(compare(o1, o2, (o) -> o
                                .getName())))
                .apply(0);
        return answer;
    }

    private <T, V extends Comparable<V>> Function<Integer, Integer> compare(T o1, T o2,
            Function<T, V> dataGetter)
    {
        return (i) -> i != 0 ? i : dataGetter.apply(o1).compareTo(dataGetter.apply(o2));
    }
}
