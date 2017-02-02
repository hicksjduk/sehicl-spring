package uk.org.sehicl.website.contacts;

import java.util.ArrayList;
import java.util.List;

public class ContactDetails
{
    private final Person person;
    private final Role role;

    public ContactDetails(Person person, Role role)
    {
        this.person = person;
        this.role = role;
    }

    public String getName()
    {
        return person.getName();
    }

    public String getAddress()
    {
        return person.getAddress();
    }

    public List<String> getPhoneNumbers()
    {
        List<String> answer = new ArrayList<>();
        for (PhoneNumber pn : person.getPhoneNumbers())
        {
            answer.add(pn.toString());
        }
        return answer;
    }

    public Role getRole()
    {
        return role;
    }
}
