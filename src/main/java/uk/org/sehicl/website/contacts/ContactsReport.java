package uk.org.sehicl.website.contacts;

import java.util.Collection;
import java.util.TreeSet;

public class ContactsReport
{
    private final Collection<ContactDetails> committeeContacts = new TreeSet<>(
            CommitteeContacts.COMPARATOR);
    private final Collection<ContactDetails> clubContacts = new TreeSet<>(
            ClubContacts.COMPARATOR);
    private final boolean restricted;

    public ContactsReport(Contacts contacts, boolean restricted)
    {
        this.restricted = restricted;
        for (Person person : contacts.getPeople())
        {
            for (Role role : person.getRoles())
            {
                if (role.getClub() == null)
                {
                    if (CommitteeContacts.ROLES.contains(role.getName()))
                    {
                        committeeContacts.add(new ContactDetails(person, role));
                    }
                }
                else
                {
                    int roleIndex = ClubContacts.roleIndex(role.getName());
                    if (roleIndex == 0 || (!restricted && roleIndex > 0))
                    {
                        clubContacts.add(new ContactDetails(person, role));
                    }
                }
            }
        }
    }

    public Collection<ContactDetails> getCommitteeContacts()
    {
        return committeeContacts;
    }

    public Collection<ContactDetails> getClubContacts()
    {
        return clubContacts;
    }

    public boolean isRestricted()
    {
        return restricted;
    }

}
