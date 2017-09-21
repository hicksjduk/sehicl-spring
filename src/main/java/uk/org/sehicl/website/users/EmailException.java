package uk.org.sehicl.website.users;

@SuppressWarnings("serial")
public class EmailException extends Exception
{
    public EmailException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public EmailException(String message)
    {
        super(message);
    }

    public EmailException(Throwable cause)
    {
        super(cause);
    }

}
