package uk.org.sehicl.website;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import redis.embedded.RedisServer;
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
        var url = EnvVar.UPSTASH_REDIS_URL.get().orElse(embeddedServerUrl());
        var datastore = new RedisDatastore(url);
        return datastore;
    }

    RedisServer dbServer = null;;

    String embeddedServerUrl()
    {
        try
        {
            var port = 6378;
            dbServer = new RedisServer(port);
            dbServer.start();
            return "redis://localhost:%d".formatted(port);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    void shutdown() throws Exception
    {
        if (dbServer != null)
            dbServer.stop();
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
            private static final Logger LOG = LoggerFactory.getLogger(EmailSender.class);

            @Override
            public void sendEmail(String subject, String messageText, Addressee... addressees)
                    throws EmailException
            {
                LOG
                        .info("Message sent to {}",
                                Stream
                                        .of(addressees)
                                        .map(Object::toString)
                                        .collect(Collectors.joining(", ")));
                LOG.info("Content: {}", messageText);
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
