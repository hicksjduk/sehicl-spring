package uk.org.sehicl.website.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TeamReference
{
    private String id;

    @JacksonXmlProperty(isAttribute = true)
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
