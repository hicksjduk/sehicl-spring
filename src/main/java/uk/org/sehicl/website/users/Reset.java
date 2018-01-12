package uk.org.sehicl.website.users;

import org.apache.commons.lang3.StringUtils;

public class Reset
{
    public static class Validation
    {
        private String password;
        private String passwordMessage;
        private String passwordConf;
        private String passwordConfMessage;

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }

        public String getPasswordMessage()
        {
            return passwordMessage;
        }

        public void setPasswordMessage(String passwordMessage)
        {
            this.passwordMessage = passwordMessage;
        }

        public String getPasswordConf()
        {
            return passwordConf;
        }

        public void setPasswordConf(String passwordConf)
        {
            this.passwordConf = passwordConf;
        }

        public String getPasswordConfMessage()
        {
            return passwordConfMessage;
        }

        public void setPasswordConfMessage(String passwordConfMessage)
        {
            this.passwordConfMessage = passwordConfMessage;
        }
    }

    private final long resetId;
    private final User user;
    private final UserManager userManager;
    private Validation validation;

    public Reset(long resetId, UserManager userManager)
    {
        this.resetId = resetId;
        this.user = userManager.getUserByResetId(resetId);
        this.userManager = userManager;
    }

    public boolean validateAndReset(String password, String passwordConf)
    {
        boolean valid = true;
        validation = new Validation();
        validation.setPassword(password);
        if (StringUtils.isEmpty(password))
        {
            validation.setPasswordMessage("Please specify your password.");
            valid = false;
        }
        if (StringUtils.isEmpty(passwordConf))
        {
            validation.setPasswordConfMessage("Please confirm your password.");
            valid = false;
        }
        if (valid)
        {
            if (!password.equals(passwordConf))
            {
                validation
                        .setPasswordConfMessage("Password and Confirm password must be the same.");
                valid = false;
            }
        }
        if (valid)
        {
            try
            {
                userManager.changePassword(user.getId(), password);
            }
            catch (UserException e)
            {
                validation.setPasswordMessage(e.getMessage());
                valid = false;
            }
        }
        return valid;
    }
    
    public Validation getValidation()
    {
        return validation;
    }

    public User getUser()
    {
        return user;
    }

    public long getResetId()
    {
        return resetId;
    }
}