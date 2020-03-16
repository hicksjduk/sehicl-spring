package uk.org.sehicl.website;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import uk.org.sehicl.website.users.EmailSender;
import uk.org.sehicl.website.users.UserDatastore;
import uk.org.sehicl.website.users.UserManager;
import uk.org.sehicl.website.users.impl.LocalDatabaseUserDatastore;
import uk.org.sehicl.website.users.impl.LocalFileSender;
import uk.org.sehicl.website.users.impl.RedisDatastore;
import uk.org.sehicl.website.users.impl.SendgridSender;

@SpringBootApplication
public class Application
{
    // private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args)
    {
        try
        {
            SpringApplication.run(Application.class, args);
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
        }
    }

    @Bean
    public UserManager userManager()
    {
        return new UserManager();
    }

    @Bean
    public UserDatastore userDatastore()
    {
        String localDbAddr = System.getProperty("localdb");
        if (StringUtils.isEmpty(localDbAddr))
            return new RedisDatastore(System.getenv("REDIS_URL"));
        return new LocalDatabaseUserDatastore(localDbAddr);
    }

    @Bean
    EmailSender emailSender()
    {
        if (System.getProperty("localmail") != null)
            return new LocalFileSender();
        return new SendgridSender();
    }
}
