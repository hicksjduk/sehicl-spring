package uk.org.sehicl.website.template;

public class PasswordResetMail
{
    private final long resetId;
    private final String resetPageAddress;

    public PasswordResetMail(long resetId, String resetPageAddress)
    {
        this.resetId = resetId;
        this.resetPageAddress = resetPageAddress;
    }

    public long getResetId()
    {
        return resetId;
    }

    public String getResetPageAddress()
    {
        return resetPageAddress;
    }

}
