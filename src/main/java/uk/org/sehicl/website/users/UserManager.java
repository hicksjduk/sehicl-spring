package uk.org.sehicl.website.users;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import uk.org.sehicl.website.template.ActivationMailTemplate;
import uk.org.sehicl.website.template.AdminNotifyMailTemplate;
import uk.org.sehicl.website.template.PasswordResetMailTemplate;
import uk.org.sehicl.website.users.EmailSender.Addressee;
import uk.org.sehicl.website.users.User.Status;
import uk.org.sehicl.website.users.UserException.Message;

public class UserManager
{
    @Autowired
    private UserDatastore datastore;

    @Autowired
    private EmailSender emailer;
    
    private final static List<String> blockList = Arrays.asList("u03.gmailmirror.com");

    public long login(String email, String password) throws UserException
    {
        datastore.clearExpiredSessions();
        User user = datastore.getUserByEmail(email);
        if (user == null)
        {
            throw new UserException(Message.emailOrPasswordNotFound);
        }
        if (!user.passwordEquals(password))
        {
            throw new UserException(Message.emailOrPasswordNotFound);
        }
        if (user.isInactive())
        {
            throw new UserException(Message.userNotActive);
        }
        long token = generateToken(user);
        return token;
    }

    private long generateToken(User user)
    {
        SessionData session = datastore.setSession(user);
        final long answer = session.getId();
        return answer;
    }

    public User registerUser(String email, String name, String club, String password, String activationPageAddress)
            throws UserException, EmailException
    {
        User answer = new User(name, email, club, Status.INACTIVE, 0, password, true);
        if (!isBlocked(email))
        {
            final User user = datastore.getUserByEmail(email);
            if (user != null)
            {
                throw new UserException(Message.emailAlreadyExists);
            }
            answer = datastore.createUser(email, name, club, Status.INACTIVE, password);
            sendActivationEmail(answer, activationPageAddress);
            notifyAdmin("register", answer);
        }
        return answer;
    }

    private boolean isBlocked(String email)
    {
        boolean answer = blockList.stream().anyMatch(email::contains);
        return answer;
    }

    private void sendActivationEmail(User user, String activationPageAddress) throws UserException
    {
        StringWriter sw = new StringWriter();
        new ActivationMailTemplate(user, activationPageAddress).process(sw);
        try
        {
            emailer.sendEmail("Activate your SEHICL account", sw.toString(),
                    new Addressee(user.getEmail(), user.getName()));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error sending activation e-mail", e);
        }
    }

    private void sendPasswordResetEmail(PasswordReset reset, String resetAddress) throws UserException, EmailException
    {
        StringWriter sw = new StringWriter();
        new PasswordResetMailTemplate(reset, resetAddress).process(sw);
        try
        {
            emailer.sendEmail("SEHICL password reset", sw.toString(),
                    new Addressee(reset.getUserEmail()));
        }
        catch (EmailException e)
        {
            throw new RuntimeException("Error sending password reset e-mail", e);
        }
    }

    private void notifyAdmin(String action, User user) throws UserException
    {
        StringWriter sw = new StringWriter();
        new AdminNotifyMailTemplate(user).process(sw);
        try
        {
            emailer.sendEmail(String.format("User action: %s", action), sw.toString(),
                    new Addressee("admin@sehicl.org.uk"));
        }
        catch (EmailException e)
        {
            throw new RuntimeException("Error sending admin notification", e);
        }
    }

    public void generatePasswordReset(String email, String resetAddress) throws UserException, EmailException
    {
        if (!isBlocked(email))
        {
            final PasswordReset reset = datastore.generatePasswordReset(email);
            if (reset == null)
            {
                throw new UserException(Message.emailNotFound);
            }
            sendPasswordResetEmail(reset, resetAddress);
        }
    }

    public User activateUser(long id) throws UserException
    {
        return setUserStatusNoNotify(id, Status.ACTIVE);
    }

    public void toggleUserStatus(long id, Status currentStatus) throws UserException, EmailException
    {
        Status newStatus = currentStatus == Status.INACTIVE ? Status.ACTIVE : Status.INACTIVE;
        setUserStatusNoNotify(id, newStatus);
    }

    private User setUserStatusNoNotify(long id, Status status) throws UserException
    {
        User answer = null;
        try
        {
            answer = setUserStatus(id, status, false);
        }
        catch (EmailException e)
        {
        }
        return answer;
    }

    private User setUserStatus(long id, Status status, boolean notifyAdmin)
            throws UserException, EmailException
    {
        final User answer = datastore.getUserById(id);
        if (answer == null)
        {
            throw new UserException(Message.userNotFound);
        }
        answer.setStatus(status);
        datastore.updateUser(answer);
        if (notifyAdmin && status == Status.ACTIVE)
        {
            notifyAdmin("activate", answer);
        }
        return answer;
    }

    public boolean sessionHasRole(Long token, String role)
    {
        datastore.clearExpiredSessions();
        boolean answer = false;
        if (token != null)
        {
            final SessionData session = datastore.getSessionBySessionId(token);
            if (session != null)
            {
                if (role == null)
                {
                    answer = true;
                }
                else
                {
                    final User user = datastore.getUserById(session.getUserId());
                    answer = user.getRoles().contains(role);
                }
            }
        }
        return answer;
    }
}
