package uk.org.sehicl.website.users;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import uk.org.sehicl.website.template.ActivationMailTemplate;
import uk.org.sehicl.website.template.AdminNotifyMailTemplate;
import uk.org.sehicl.website.template.PasswordResetMailTemplate;
import uk.org.sehicl.website.template.ReconfirmationMailTemplate;
import uk.org.sehicl.website.users.EmailSender.Addressee;
import uk.org.sehicl.website.users.User.Status;
import uk.org.sehicl.website.users.UserException.Message;

public class UserManager
{
    private static final Logger LOG = LoggerFactory.getLogger(UserManager.class);
    
    public static Stream<User> fromFile(String fileName)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            ArrayType type = mapper.getTypeFactory().constructArrayType(User.class);
            User[] array = (User[]) mapper.readValue(br, type);
            LOG.info("Loaded {} user(s) from {}", array.length, fileName);
            return Stream.of(array);
        }
        catch (FileNotFoundException ex)
        {
            LOG.info("File {} not found", fileName);
            return Stream.empty();
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Unable to load users from file", ex);
        }
    }

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

    public User registerUser(String email, String name, String club, String password,
            String serverAddress) throws UserException, EmailException
    {
        User answer = new User(name, email, club, Status.INACTIVE, 0, password, true);
        if (!isBlocked(answer))
        {
            final User user = datastore.getUserByEmail(email);
            if (user != null)
            {
                throw new UserException(Message.emailAlreadyExists);
            }
            answer = datastore.createUser(email, name, club, Status.INACTIVE, password);
            sendActivationEmail(answer, serverAddress);
            notifyAdmin("register", answer, serverAddress);
        }
        return answer;
    }

    private boolean isBlocked(User user)
    {
        return false;
        // if (isBlocked(user.getEmail()))
        // return true;
        // return StringUtils.equals(user.getName(), user.getClub());
    }

    private boolean isBlocked(String email)
    {
        boolean answer = blockList.stream().anyMatch(email::contains);
        return answer;
    }

    private void sendActivationEmail(User user, String serverAddress)
            throws UserException, EmailException
    {
        StringWriter sw = new StringWriter();
        new ActivationMailTemplate(user, serverAddress).process(sw);
        emailer
                .sendEmail("Activate your SEHICL account", sw.toString(),
                        Addressee.withAddress(user.getEmail()).withName(user.getName()));
    }

    private void sendPasswordResetEmail(PasswordReset reset, String resetAddress)
            throws UserException, EmailException
    {
        StringWriter sw = new StringWriter();
        new PasswordResetMailTemplate(reset, resetAddress).process(sw);
        emailer
                .sendEmail("SEHICL password reset", sw.toString(),
                        Addressee.withAddress(reset.getUserEmail()));
    }

    private void notifyAdmin(String action, User user, String serverAddress)
    {
        StringWriter sw = new StringWriter();
        new AdminNotifyMailTemplate(user, serverAddress).process(sw);
        try
        {
            emailer
                    .sendEmail(String.format("User action: %s", action), sw.toString(),
                            Addressee.withAddress("admin@sehicl.org.uk"));
        }
        catch (Exception e)
        {
            throw (new RuntimeException("Unexpected exception", e));
        }
    }

    public void generatePasswordReset(String email, String resetAddress)
            throws UserException, EmailException
    {
        datastore.clearExpiredResets();
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
            answer = setUserStatus(id, status, null, false);
        }
        catch (EmailException e)
        {
        }
        return answer;
    }

    private User setUserStatus(long id, Status status, String userDetailsPageAddress,
            boolean notifyAdmin) throws UserException, EmailException
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
            notifyAdmin("activate", answer, userDetailsPageAddress);
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

    public void changePassword(long userId, String newPassword) throws UserException
    {
        final User user = datastore.getUserById(userId);
        if (user == null)
        {
            throw new UserException(Message.userNotFound);
        }
        user.encodeAndSetPassword(newPassword);
        datastore.updateUser(user);
    }

    public User getUserByResetId(long resetId)
    {
        datastore.clearExpiredResets();
        return Optional
                .ofNullable(datastore.getPasswordReset(resetId))
                .map(PasswordReset::getUserId)
                .map(datastore::getUserById)
                .orElse(null);
    }

    public User getUserById(long userId)
    {
        return datastore.getUserById(userId);
    }

    public User reconfirmUser(long id) throws UserException, EmailException
    {
        final User answer = setUserStatusNoNotify(id, Status.ACTIVE);
        notifyAdmin("reconfirm", answer, null);
        return answer;
    }

    public void sendReconfirmationEmails(String reconfirmationPageAddress)
    {
        datastore.getAllUserIds().forEach(id ->
        {
            try
            {
                User user = setUserStatusNoNotify(id, Status.AWAITING_RECONFIRMATION);
                StringWriter sw = new StringWriter();
                new ReconfirmationMailTemplate(user, reconfirmationPageAddress).process(sw);
                emailer
                        .sendEmail("ACTION REQUIRED - Reconfirm your SEHICL account", sw.toString(),
                                Addressee.withAddress(user.getEmail()).withName(user.getName()));
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error sending activation email", e);
            }
        });
    }

    public void deleteUser(long userId)
    {
        datastore.deleteUser(userId);
    }

    public Stream<User> allUsers()
    {
        return datastore.getAllUserIds().stream().map(datastore::getUserById);
    }
}
