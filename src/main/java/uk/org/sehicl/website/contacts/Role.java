package uk.org.sehicl.website.contacts;

public class Role
{
    private String name;
    private String club;
    private Email email;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getClub()
    {
        return club;
    }

    public void setClub(String club)
    {
        this.club = club;
    }

    public Email getEmail()
    {
        return email;
    }

    public void setEmail(Email email)
    {
        this.email = email;
    }

    public static class Email
    {
        private String id;
        private String domain;

        public void setId(String id)
        {
            this.id = id;
        }

        public void setDomain(String domain)
        {
            this.domain = domain;
        }

        public String getId()
        {
            return id;
        }

        public String getDomain()
        {
            return domain;
        }
    }
}
