package uk.org.sehicl.website.contacts;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Contacts
{
    private static Contacts INSTANCE = null;

    public static Contacts getContacts()
    {
        if (INSTANCE == null)
        {
            final String fileName = "data/contacts.xml";
            InputStream source = loadFromClasspath(fileName);
            try (InputStream str = source)
            {
                INSTANCE = new XmlMapper().readValue(str, Contacts.class);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Unable to load contacts", e);
            }
        }
        return INSTANCE;
    }

    private String defaultDomain;
    private final List<Person> people = new ArrayList<>();

    public String getDefaultDomain()
    {
        return defaultDomain;
    }

    public void setDefaultDomain(String defaultDomain)
    {
        this.defaultDomain = defaultDomain;
    }

    public List<Person> getPeople()
    {
        return people;
    }

    @JacksonXmlProperty(localName = "person")
    public void addPerson(Person person)
    {
        people.add(person);
    }

    private static InputStream loadFromClasspath(String fileName)
    {
        InputStream answer = Contacts.class.getClassLoader().getResourceAsStream(fileName);
        return answer;
    }
}
