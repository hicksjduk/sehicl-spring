package uk.org.sehicl.website.contacts;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Person
{
    private String name;
    private String address;
    private final List<PhoneNumber> phoneNumbers = new ArrayList<>();
    private final List<Role> roles = new ArrayList<>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public List<PhoneNumber> getPhoneNumbers()
    {
        return phoneNumbers;
    }

    @JacksonXmlProperty(localName = "phone")
    public void addPhoneNumber(String phoneNumber)
    {
        phoneNumbers.add(new PhoneNumber(phoneNumber));
    }

    public List<Role> getRoles()
    {
        return roles;
    }

    @JacksonXmlProperty(localName = "role")
    public void addRole(Role role)
    {
        roles.add(role);
    }
}
