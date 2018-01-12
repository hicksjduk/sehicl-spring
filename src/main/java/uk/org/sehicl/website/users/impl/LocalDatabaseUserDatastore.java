package uk.org.sehicl.website.users.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import uk.org.sehicl.website.users.PasswordReset;
import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.User.Status;
import uk.org.sehicl.website.users.UserDatastore;

public class LocalDatabaseUserDatastore implements UserDatastore
{
    private final String dbUrl;

    public LocalDatabaseUserDatastore(String dbAddress)
    {
        dbUrl = String.format("jdbc:sqlite:%s", dbAddress);
    }

    private Connection connect() throws SQLException
    {
        return DriverManager.getConnection(dbUrl);
    }

    @Override
    public User getUserByEmail(String email)
    {
        User answer = null;
        try (Connection conn = connect())
        {
            final PreparedStatement stmt = conn.prepareStatement(
                    "select u.id, u.name, u.email, u.club, u.status, u.failures, u.password, r.role "
                            + "from user u left outer join role r " + "on u.id = r.user_id "
                            + "where u.email = ?");
            stmt.setString(1, email);
            final ResultSet rs = stmt.executeQuery();
            answer = getUser(rs);
        }
        catch (SQLException ex)
        {
            throw new RuntimeException("Error retrieving user by email", ex);
        }
        return answer;
    }

    private User getUser(ResultSet rs) throws SQLException
    {
        User answer = null;
        if (rs.next())
        {
            int index = 1;
            answer = new User(rs.getLong(index++), rs.getString(index++), rs.getString(index++),
                    rs.getString(index++), Status.valueOf(rs
                            .getString(index++)
                            .toUpperCase()),
                    rs.getInt(index++), rs.getString(index++), false);
            do
            {
                final String role = rs.getString(index);
                if (role != null)
                {
                    answer.getRoles().add(role);
                }
            }
            while (rs.next());
        }
        return answer;
    }

    @Override
    public User getUserById(long id)
    {
        User answer = null;
        try (Connection conn = connect())
        {
            final PreparedStatement stmt = conn.prepareStatement(
                    "select id, u.name, u.email, u.club, u.status, u.failures, u.password, r.role "
                            + "from user u left outer join role r " + "on u.id = r.user_id "
                            + "where u.id = ?");
            stmt.setLong(1, id);
            final ResultSet rs = stmt.executeQuery();
            answer = getUser(rs);
        }
        catch (SQLException ex)
        {
            throw new RuntimeException("Error retrieving user by ID", ex);
        }
        return answer;
    }

    public List<Long> getUserKeys()
    {
        List<Long> answer = new LinkedList<>();
        try (Connection conn = connect())
        {
            final ResultSet rs = conn.createStatement().executeQuery(
                    "select id from user");
            while (rs.next())
            {
                answer.add(rs.getLong(1));
            }
        }
        catch (SQLException ex)
        {
            throw new RuntimeException("Error retrieving userKeys", ex);
        }
        return answer;
    }

    private SessionData getSession(ResultSet rs) throws SQLException
    {
        SessionData answer = null;
        if (rs.next())
        {
            int index = 1;
            answer = new SessionData(rs.getLong(index++), rs.getLong(index++), rs.getLong(index++));
        }
        return answer;
    }

    @Override
    public SessionData getSessionByUserId(long id)
    {
        SessionData answer = null;
        try (Connection conn = connect())
        {
            final PreparedStatement stmt = conn.prepareStatement(
                    "select id, user_id, expiry " + "from session " + "where user_id = ?");
            stmt.setLong(1, id);
            final ResultSet rs = stmt.executeQuery();
            answer = getSession(rs);
        }
        catch (SQLException ex)
        {
            throw new RuntimeException("Error retrieving session by user ID", ex);
        }
        return answer;
    }

    @Override
    public SessionData getSessionBySessionId(long id)
    {
        SessionData answer = null;
        try (Connection conn = connect())
        {
            final PreparedStatement stmt = conn.prepareStatement(
                    "select id, user_id, expiry " + "from session " + "where id = ?");
            stmt.setLong(1, id);
            final ResultSet rs = stmt.executeQuery();
            answer = getSession(rs);
        }
        catch (SQLException ex)
        {
            throw new RuntimeException("Error retrieving session by ID", ex);
        }
        return answer;
    }

    @Override
    public SessionData setSession(User user)
    {
        try (Connection conn = connect())
        {
            final PreparedStatement stmt = conn
                    .prepareStatement("insert into session (expiry, user_id) values (?, ?)");
            final long expiry = new Date().getTime() + TimeUnit.DAYS.toMillis(1);
            stmt.setLong(1, expiry);
            stmt.setLong(2, user.getId());
            stmt.execute();
            final ResultSet rs = conn.createStatement().executeQuery("select last_insert_rowid()");
            rs.next();
            final long sessionId = rs.getLong(1);
            return new SessionData(sessionId, user.getId(), expiry);
        }
        catch (SQLException ex)
        {
            throw new RuntimeException("Error creating session", ex);
        }
    }

    @Override
    public void clearExpiredSessions()
    {
        try (Connection conn = connect())
        {
            final PreparedStatement stmt = conn
                    .prepareStatement("delete from session where expiry < ?");
            stmt.setLong(1, new Date().getTime());
            stmt.execute();
        }
        catch (SQLException ex)
        {
            throw new RuntimeException("Error clearing expired sessions", ex);
        }
    }

    @Override
    public User createUser(String email, String name, String club, Status status, String password)
    {
        try (Connection conn = connect())
        {
            final PreparedStatement stmt = conn.prepareStatement("insert into user "
                    + "(club, email, password, status, name) values (?, ?, ?, ?, ?)");
            int index = 1;
            stmt.setString(index++, club);
            stmt.setString(index++, email);
            stmt.setString(index++, password);
            stmt.setString(index++, status.toString());
            stmt.setString(index++, name);
            stmt.execute();
            final ResultSet rs = conn.createStatement().executeQuery("select last_insert_rowid()");
            rs.next();
            final long id = rs.getLong(1);
            return new User(id, name, email, club, status, 0, password, true);
        }
        catch (SQLException ex)
        {
            throw new RuntimeException("Error creating user", ex);
        }
    }

    @Override
    public void updateUser(User user)
    {
        try (Connection conn = connect())
        {
            final PreparedStatement stmt = conn.prepareStatement("update user "
                    + "set club = ?, email = ?, password = ?, status = ?, name = ?, failures = ?) "
                    + "where id = ?");
            int index = 1;
            stmt.setString(index++, user.getClub());
            stmt.setString(index++, user.getEmail());
            stmt.setString(index++, user.getPassword());
            stmt.setString(index++, user.getStatus().toString());
            stmt.setString(index++, user.getName());
            stmt.setInt(index++, user.getFailureCount());
            stmt.setLong(index++, user.getId());
            stmt.execute();
        }
        catch (SQLException ex)
        {
            throw new RuntimeException("Error updating user", ex);
        }
    }

    @Override
    public PasswordReset generatePasswordReset(String email)
    {
        PasswordReset answer = null;
        final User user = getUserByEmail(email);
        if (user != null)
        {
            answer = new PasswordReset(user.getId(), email);
        }
        return answer;
    }

    @Override
    public PasswordReset getPasswordReset(long id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clearExpiredResets()
    {
        // TODO Auto-generated method stub
        
    }

}
