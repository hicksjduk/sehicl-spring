package uk.org.sehicl.website.page;

import uk.org.sehicl.website.contacts.Contacts;
import uk.org.sehicl.website.contacts.ContactsReport;
import uk.org.sehicl.website.navigator.Section;

public class ContactsPage extends Page
{
    private final ContactsReport contacts;

    public ContactsPage()
    {
        super("contacts", "contacts.ftlh", Section.CONTACTS);
        contacts = new ContactsReport(Contacts.getContacts(), true);
    }

    @Override
    public String getTitle()
    {
        return "SEHICL Contacts";
    }

    public ContactsReport getContacts()
    {
        return contacts;
    }

}
