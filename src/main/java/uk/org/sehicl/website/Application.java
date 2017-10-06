package uk.org.sehicl.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import uk.org.sehicl.website.users.EmailSender;
import uk.org.sehicl.website.users.UserDatastore;
import uk.org.sehicl.website.users.UserManager;
import uk.org.sehicl.website.users.impl.JavamailSender;
import uk.org.sehicl.website.users.impl.RedisDatastore;

@SpringBootApplication
public class Application
{
//    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

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
        final String redisUrl = System.getenv("REDIS_URL");
        System.out.println("Redis URL: " + redisUrl);
        return new RedisDatastore(redisUrl);
    }

    @Bean
    EmailSender emailSender()
    {
        return new JavamailSender();
    }
}
