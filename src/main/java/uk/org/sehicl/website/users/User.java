package uk.org.sehicl.website.users;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("users")
public class User
{
    public static enum Status
    {
        INACTIVE, ACTIVE
    }

    @Id
    private Long id;
    private final String name;
    @Indexed
    private final String email;
    private final String club;
    private final List<String> roles = new ArrayList<>();
    private Status status;
    private final int failureCount;
    private final String password;

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

    public boolean isInactive()
    {
        return status == Status.INACTIVE;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }
}
