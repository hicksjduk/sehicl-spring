package uk.org.sehicl.website;

public enum EnvVar
{
    PORT, REDIS_URL, ADMIN_SECRET, RECAPTCHA_SECRET, SENDGRID_API_KEY;
    
    public String get()
    {
        return System.getenv(name());
    }
}
