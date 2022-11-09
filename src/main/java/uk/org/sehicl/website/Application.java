package uk.org.sehicl.website;

import java.util.Collections;
import java.util.Objects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import uk.org.sehicl.admin.UsersExporter;
import uk.org.sehicl.admin.UsersImporter;
import uk.org.sehicl.website.users.EmailSender;
import uk.org.sehicl.website.users.UserDatastore;
import uk.org.sehicl.website.users.UserManager;
import uk.org.sehicl.website.users.impl.GoogleCloudDatastore;
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
            SpringApplication app = new SpringApplication(Application.class);
            String port = System.getenv("PORT");
            if (port != null)
                app.setDefaultProperties(Collections.singletonMap("server.port", port));
            app.run(args);
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
        String redisAddr = System.getenv("REDIS_URL");
        if (Objects.nonNull(redisAddr))
            return new RedisDatastore(redisAddr);
        return new GoogleCloudDatastore();
    }

    @Bean
    EmailSender emailSender()
    {
        return new SendgridSender();
    }

    @Bean
    public UsersExporter usersExporter(UserManager userManager)
    {
        return new UsersExporter(userManager);
    }

    @Bean
    public UsersImporter usersImporter(UserDatastore datastore)
    {
        return new UsersImporter(datastore);
    }
    
    @Bean
    public SehiclEnvironment environmentVars()
    {
        return new SehiclEnvironment();
    }
}
