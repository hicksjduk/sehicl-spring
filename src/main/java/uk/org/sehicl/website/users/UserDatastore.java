package uk.org.sehicl.website.users;

import uk.org.sehicl.website.users.User.Status;

public interface UserDatastore
{
    User getUserByEmail(String email);

    User getUserById(long id);

    SessionData getSessionByUserId(long id);

    SessionData getSessionBySessionId(long id);

    SessionData setSession(User user);

    void clearExpiredSessions();

    User createUser(String email, String name, String club, Status status,
            String password);

    void updateUser(User user);
    
    PasswordReset generatePasswordReset(String email);
    
    PasswordReset getPasswordReset(long id);

    void clearExpiredResets();
}
