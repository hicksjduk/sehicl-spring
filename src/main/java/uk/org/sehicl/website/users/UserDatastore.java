package uk.org.sehicl.website.users;

import uk.org.sehicl.website.users.User.Status;

public interface UserDatastore
{
    User getUserByEmail(String email);

    User getUserById(long id);

    UserSession getSessionByUserId(long id);

    UserSession getSessionBySessionId(long id);

    UserSession createSession(User user);

    void clearExpiredSessions();

    User createUser(String email, String name, String club, Status status,
            String password);

    void updateUser(User user);
}
