package uk.org.sehicl.website.users;

import org.apache.commons.lang3.StringUtils;

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
    
    private final UserManager userManager;

    public Login(UserManager userManager)
    {
        this(userManager, null, null);
    }

    public Login(UserManager userManager, String email, String password)
    {
        this.userManager = userManager;
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
            validation.setEmailMessage("Please specify your email address.");
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

    public void validateAndReset(String resetPageAddress)
    {
        boolean allFieldsPresent = true;
        if (StringUtils.isEmpty(validation.email))
        {
            validation.setEmailMessage("Please specify your email address.");
            allFieldsPresent = false;
        }
        if (allFieldsPresent)
        {
            try
            {
                userManager.generatePasswordReset(validation.email, resetPageAddress);
                validation.setEmailMessage("A reset email has been sent to this address.");
            }
            catch (UserException | EmailException e)
            {
                validation.setEmailMessage(e.getMessage());
            }
        }
    }
}
