package uk.org.sehicl.website.users;

@SuppressWarnings("serial")
public class UserException extends Exception
{
    public UserException(Message message)
    {
        super(message.message);
    }

    public UserException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public static enum Message
    {
        emailNotFound("Email address not found"), emailOrPasswordNotFound(
                "Email address and/or password not found"), userNotActive(
                        "User is not active - you must activate your ID before you can login"), sessionExpired(
                                "Session expired"), emailAlreadyExists(
                                        "A user with this e-mail address already exists"), userNotFound(
                                                "User not found"), resetTokenNotFound(
                                                        "Specified reset token does not exist, or has expired");

        private final String message;

        private Message(String message)
        {
            this.message = message;
        }
    }
}
