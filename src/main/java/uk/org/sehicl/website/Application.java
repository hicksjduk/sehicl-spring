package uk.org.sehicl.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import uk.org.sehicl.website.users.EmailSender;
import uk.org.sehicl.website.users.UserDatastore;
import uk.org.sehicl.website.users.UserManager;
import uk.org.sehicl.website.users.impl.JavamailSender;
import uk.org.sehicl.website.users.impl.LocalDatabaseUserDatastore;

@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public UserManager userManager()
    {
        return new UserManager();
    }

    @Bean
    public UserDatastore userDatastore()
    {
        return new LocalDatabaseUserDatastore("/Users/jerhicks/Documents/personal/sehicl/users.db");
    }

    @Bean
    EmailSender emailSender()
    {
        return new JavamailSender();
    }
}
