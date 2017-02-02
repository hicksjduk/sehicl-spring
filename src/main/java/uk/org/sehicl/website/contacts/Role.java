package uk.org.sehicl.website.contacts;

public class Role
{
    private String name;
    private String club;
    private String email;

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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(Email email)
    {
        this.email = email.id;
    }

    public static class Email
    {
        private String id;

        public void setId(String id)
        {
            this.id = id;
        }
    }
}
