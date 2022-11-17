package uk.org.sehicl.website;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args)
    {
        try
        {
            SpringApplication app = new SpringApplication(Application.class);
            Optional
                    .<Object> ofNullable(EnvVar.PORT.get())
                    .map(p -> Collections.singletonMap("server.port", p))
                    .ifPresent(app::setDefaultProperties);
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
        return Optional
                .ofNullable(EnvVar.REDIS_URL.get())
                .<UserDatastore> map(RedisDatastore::new)
                .orElseGet(GoogleCloudDatastore::new);
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
}
