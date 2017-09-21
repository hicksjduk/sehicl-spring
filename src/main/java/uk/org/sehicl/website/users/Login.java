package uk.org.sehicl.website.users;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class Login
{
    public static class Validation
    {
        private String email;
        private String password;
        private String emailMessage;
        private String passwordMessage;

        public String getEmail()
        {
            return email;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }

        public String getEmailMessage()
        {
            return emailMessage;
        }

        public void setEmailMessage(String emailMessage)
        {
            this.emailMessage = emailMessage;
        }

        public String getPasswordMessage()
        {
            return passwordMessage;
        }

        public void setPasswordMessage(String passwordMessage)
        {
            this.passwordMessage = passwordMessage;
        }
    }

    public final Validation validation = new Validation();
    public String message;
    
    @Autowired
    private UserManager userManager;

    public Login()
    {
        this(null, null);
    }

    public Login(String email, String password)
    {
        validation.email = email;
        validation.password = password;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Validation getValidation()
    {
        return validation;
    }

    public Long validateAndLogin()
    {
        boolean allFieldsPresent = true;
        Long answer = null;
        if (StringUtils.isEmpty(validation.email))
        {
            validation.setEmailMessage("Please specify your e-mail address.");
            allFieldsPresent = false;
        }
        if (StringUtils.isEmpty(validation.password))
        {
            validation.setPasswordMessage("Please specify your password.");
            allFieldsPresent = false;
        }
        if (allFieldsPresent)
        {
            try
            {
                answer = userManager.login(validation.email, validation.password);
            }
            catch (UserException e)
            {
                validation.setEmailMessage(e.getMessage());
            }
        }
        return answer;
    }

    public void validateAndRemind() throws EmailException
    {
        boolean allFieldsPresent = true;
        if (StringUtils.isEmpty(validation.email))
        {
            validation.setEmailMessage("Please specify your e-mail address.");
            allFieldsPresent = false;
        }
        if (allFieldsPresent)
        {
            try
            {
                userManager.remindOfPassword(validation.email);
                validation.setEmailMessage("A reminder e-mail has been sent to this address.");
            }
            catch (UserException e)
            {
                validation.setEmailMessage(e.getMessage());
            }
        }
    }
}
