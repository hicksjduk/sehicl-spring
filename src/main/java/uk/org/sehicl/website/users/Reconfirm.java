package uk.org.sehicl.website.users;

public class Reconfirm
{
    public static class Validation
    {
        private boolean agreement;
        private String agreementMessage;

        public boolean getAgreement()
        {
            return agreement;
        }

        public void setAgreement(boolean agreement)
        {
            this.agreement = agreement;
        }

        public String getAgreementMessage()
        {
            return agreementMessage;
        }

        public void setAgreementMessage(String agreementMessage)
        {
            this.agreementMessage = agreementMessage;
        }

    }

    private final long userId;
    private final UserManager userManager;
    private final User user;
    private Validation validation;

    public Reconfirm(long userId, UserManager userManager)
    {
        this.userId = userId;
        this.user = userManager.getUserById(userId);
        this.userManager = userManager;
    }

    public boolean validateAndReconfirm(boolean agreed)
    {
        boolean valid = true;
        validation = new Validation();
        validation.setAgreement(agreed);
        if (!agreed)
        {
            validation.setAgreementMessage("You must tick the box to continue.");
            valid = false;
        }
        if (valid)
        {
            try
            {
                userManager.reconfirmUser(userId);
            }
            catch (UserException e)
            {
                valid = false;
            }
        }
        return valid;
    }

    public Validation getValidation()
    {
        return validation;
    }

    public long getUserId()
    {
        return userId;
    }

    public UserManager getUserManager()
    {
        return userManager;
    }

    public User getUser()
    {
        return user;
    }

}