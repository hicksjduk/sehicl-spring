package uk.org.sehicl.website.users;

import org.apache.commons.lang3.StringUtils;

public class Register
{
    public static class Validation
    {
        private String email;
        private String name;
        private String club;
        private String password;
        private String passwordConf;
        private boolean agreement;
        private String emailMessage;
        private String nameMessage;
        private String clubMessage;
        private String passwordMessage;
        private String passwordConfMessage;
        private String agreementMessage;

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

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getClub()
        {
            return club;
        }

        public void setClub(String club)
        {
            this.club = club;
        }

        public String getPasswordConf()
        {
            return passwordConf;
        }

        public void setPasswordConf(String passwordConf)
        {
            this.passwordConf = passwordConf;
        }

        public String getNameMessage()
        {
            return nameMessage;
        }

        public void setNameMessage(String nameMessage)
        {
            this.nameMessage = nameMessage;
        }

        public String getClubMessage()
        {
            return clubMessage;
        }

        public void setClubMessage(String clubMessage)
        {
            this.clubMessage = clubMessage;
        }

        public String getPasswordConfMessage()
        {
            return passwordConfMessage;
        }

        public void setPasswordConfMessage(String passwordConfMessage)
        {
            this.passwordConfMessage = passwordConfMessage;
        }

        public String getAgreementMessage()
        {
            return agreementMessage;
        }

        public void setAgreementMessage(String agreementMessage)
        {
            this.agreementMessage = agreementMessage;
        }

        public boolean getAgreement()
        {
            return agreement;
        }

        public void setAgreement(boolean agreement)
        {
            this.agreement = agreement;
        }
    }

    private final Validation validation = new Validation();
    private String message;

    private final UserManager userManager;

    public Register(UserManager userManager)
    {
        this(userManager, null, null, null, null, null, false);
    }

    public Register(UserManager userManager, String email, String name, String club,
            String password, String passwordConf, boolean agreement)
    {
        this.userManager = userManager;
        validation.email = email;
        validation.name = name;
        validation.club = StringUtils.isEmpty(club) ? null : club;
        validation.password = password;
        validation.passwordConf = passwordConf;
        validation.agreement = agreement;
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

    public User validateAndRegister(String serverAddress)
    {
        User answer = null;
        boolean valid = true;
        if (StringUtils.isEmpty(validation.name))
        {
            validation.setNameMessage("Please specify your name.");
            valid = false;
        }
        if (StringUtils.isEmpty(validation.email))
        {
            validation.setEmailMessage("Please specify your e-mail address.");
            valid = false;
        }
        if (StringUtils.isEmpty(validation.password))
        {
            validation.setPasswordMessage("Please specify your password.");
            valid = false;
        }
        if (StringUtils.isEmpty(validation.passwordConf))
        {
            validation.setPasswordConfMessage("Please confirm your password.");
            valid = false;
        }
        if (!validation.agreement)
        {
            validation.setAgreementMessage("You must tick the box to register.");
            valid = false;
        }
        if (valid)
        {
            if (!validation.password.equals(validation.passwordConf))
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
                answer = userManager
                        .registerUser(validation.email, validation.name, validation.club,
                                validation.password, serverAddress);
            }
            catch (UserException e)
            {
                validation.setEmailMessage(e.getMessage());
            }
            catch (EmailException e)
            {
                validation.setEmailMessage(e.getMessage());
                valid = false;
            }
        }
        return answer;
    }
}
