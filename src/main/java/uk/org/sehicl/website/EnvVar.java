package uk.org.sehicl.website;

import java.util.Optional;

public enum EnvVar
{
    PORT,
    REDISCLOUD_URL,
    ADMIN_SECRET,
    RECAPTCHA_SECRET,
    SENDGRID_API_KEY,
    BLOCK_INDEXING,
    FORCE_HTTPS,
    LOCAL_DATASTORE,
    USERS_BUCKET;

    public Optional<String> get()
    {
        return Optional.of(name()).map(System::getenv);
    }

    public String getAsString()
    {
        return get().orElse(null);
    }
}
