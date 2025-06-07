package uk.org.sehicl.website;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import uk.org.sehicl.admin.UsersExporter;
import uk.org.sehicl.admin.UsersImporter;
import uk.org.sehicl.website.users.EmailException;
import uk.org.sehicl.website.users.EmailSender;
import uk.org.sehicl.website.users.UserDatastore;
import uk.org.sehicl.website.users.UserManager;
import uk.org.sehicl.website.users.impl.MailgunSender;
import uk.org.sehicl.website.users.impl.RedisDatastore;

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
            EnvVar.PORT
                    .get()
                    .map((Object p) -> Collections.singletonMap("server.port", p))
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
        return EnvVar.UPSTASH_REDIS_REST_URL
                .get()
                .map(uri -> new RedisDatastore(uri,
                        EnvVar.UPSTASH_REDIS_REST_TOKEN.get().orElse("hello")))
                .orElse(null);
    }

    @Bean
    EmailSender emailSender()
    {
        return EnvVar.MAILGUN_API_KEY
                .get()
                .map(apiKey -> EnvVar.MAILGUN_DOMAIN
                        .get()
                        .<EmailSender> map(domain -> new MailgunSender(apiKey, domain))
                        .orElse(dummySender()))
                .orElse(dummySender());
    }

    private EmailSender dummySender()
    {
        return new EmailSender()
        {

            @Override
            public void sendEmail(String subject, String messageText, Addressee... addressees)
                    throws EmailException
            {
            }

        };
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
