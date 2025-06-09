package uk.org.sehicl.website;

import java.util.Optional;

public enum EnvVar
{
    PORT,
    UPSTASH_REDIS_URL,
    MAILGUN_API_KEY,
    MAILGUN_DOMAIN,
    ADMIN_SECRET,
    RECAPTCHA_SECRET,
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
