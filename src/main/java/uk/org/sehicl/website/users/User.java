package uk.org.sehicl.website.users;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User
{
    public static enum Status
    {
        INACTIVE, ACTIVE, AWAITING_RECONFIRMATION
    }

    private Long id;
    private String name;
    private String email;
    private String club;
    private final List<String> roles = new ArrayList<>();
    private Status status;
    private int failureCount;
    private String password;

    public User()
    {
    }
    
    public User(String name, String email, String club, Status status, int failureCount,
            String password, boolean encodePassword)
    {
        this(null, name, email, club, status, failureCount, password, encodePassword);
    }

    public User(Long id, String name, String email, String club, Status status, int failureCount,
            String password, boolean encodePassword)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.club = club;
        this.status = status;
        this.failureCount = failureCount;
        this.password = encodePassword ? base64Encode(password) : password;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getClub()
    {
        return club;
    }

    public List<String> getRoles()
    {
        return roles;
    }

    public String getPassword()
    {
        return password;
    }

    @JsonIgnore
    public String getDecodedPassword()
    {
        return new String(Base64.getDecoder().decode(password));
    }

    public Status getStatus()
    {
        return status;
    }

    public int getFailureCount()
    {
        return failureCount;
    }

    public boolean passwordEquals(String password)
    {
        return this.password.equals(base64Encode(password));
    }

    private String base64Encode(String password)
    {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    @JsonIgnore
    public boolean isInactive()
    {
        return status != Status.ACTIVE;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    private void setPassword(String password)
    {
        this.password = password;
    }
    
    public void encodeAndSetPassword(String password)
    {
        setPassword(base64Encode(password));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((club == null) ? 0 : club.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + failureCount;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((roles == null) ? 0 : roles.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (club == null)
        {
            if (other.club != null)
                return false;
        }
        else if (!club.equals(other.club))
            return false;
        if (email == null)
        {
            if (other.email != null)
                return false;
        }
        else if (!email.equals(other.email))
            return false;
        if (failureCount != other.failureCount)
            return false;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (password == null)
        {
            if (other.password != null)
                return false;
        }
        else if (!password.equals(other.password))
            return false;
        if (roles == null)
        {
            if (other.roles != null)
                return false;
        }
        else if (!roles.equals(other.roles))
            return false;
        if (status != other.status)
            return false;
        return true;
    }
}
