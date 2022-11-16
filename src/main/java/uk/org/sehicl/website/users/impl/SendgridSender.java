package uk.org.sehicl.website.users.impl;

import java.io.IOException;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

import uk.org.sehicl.website.EnvironmentVars;
import uk.org.sehicl.website.users.EmailException;
import uk.org.sehicl.website.users.EmailSender;

public class SendgridSender implements EmailSender
{
    private static final Logger LOG = LoggerFactory.getLogger(SendgridSender.class);
    
    @Value("${sendgrid.server:}")
    private String sendGridServer;
    
    private final EnvironmentVars envVars;
    
    public SendgridSender(EnvironmentVars envVars)
    {
        this.envVars = envVars;
    }

    @Override
    public void sendEmail(String subject, String messageText, Addressee... addressees)
            throws EmailException
    {
        Email from = new Email("admin@sehicl.org.uk", "SEHICL Admin");
        Content content = new Content("text/html", messageText);
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);
        final Personalization mailInfo = new Personalization();
        Stream.of(addressees).map(this::getEmail).forEach(mailInfo::addTo);
        mail.addPersonalization(mailInfo);
        send(mail);
    }
    
    private void send(Mail mail) throws EmailException
    {
        boolean serverConfigured = !StringUtils.isEmpty(sendGridServer);
        String apiKey = envVars.get("SENDGRID_API_KEY");
        SendGrid sg = new SendGrid(apiKey, serverConfigured);
        if (serverConfigured)
            sg.setHost(sendGridServer);
        Request req = new Request();
        req.setMethod(Method.POST);
        req.setEndpoint("mail/send");
        try
        {
            req.setBody(mail.build());
            sg.api(req);
        }
        catch (IOException e)
        {
            LOG.error("Unable to send email message", e);
            throw new EmailException("Unable to send email message", e);
        }
    }

    private Email getEmail(Addressee addr)
    {
        Email answer;
        answer = addr.getName() == null ? new Email(addr.getAddress())
                : new Email(addr.getAddress(), addr.getName());
        return answer;
    }
}
