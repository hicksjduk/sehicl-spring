package uk.org.sehicl.website.template;

import java.io.OutputStreamWriter;

import org.junit.Test;

import uk.org.sehicl.website.page.ContactsPage;
import uk.org.sehicl.website.page.HomePage;

public class TemplateTest
{
    public void testHomePage()
    {
        new Template(new HomePage("/")).process(new OutputStreamWriter(System.out));
    }

    @Test
    public void testContactsPage()
    {
        new Template(new ContactsPage("/contacts")).process(new OutputStreamWriter(System.out));
    }
}
