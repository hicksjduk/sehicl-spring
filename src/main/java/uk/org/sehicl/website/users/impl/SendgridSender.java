package uk.org.sehicl.website.users.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

import uk.org.sehicl.website.EnvVar;
import uk.org.sehicl.website.users.EmailException;
import uk.org.sehicl.website.users.EmailSender;

public class SendgridSender implements EmailSender
{
    private static final Logger LOG = LoggerFactory.getLogger(SendgridSender.class);

    @Value("${sendgrid.server:}")
    private String sendGridServer;

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
        String apiKey = EnvVar.SENDGRID_API_KEY.get();
        SendGrid sg = new SendGrid(apiKey, serverConfigured);
        if (serverConfigured)
            sg.setHost(sendGridServer);
        Request req = new Request();
        req.setMethod(Method.POST);
        req.setEndpoint("mail/send");
        List<String> addressees = mail
                .getPersonalization()
                .stream()
                .map(Personalization::getTos)
                .flatMap(Collection::stream)
                .map(Email::getEmail)
                .toList();
        LOG.info("Sending mail: {} to {}", mail.getSubject(), addressees);
        try
        {
            req.setBody(mail.build());
            sg.api(req);
        }
        catch (IOException e)
        {
            LOG.error("Unable to send email message {} to {}", mail.getSubject(), addressees, e);
//            throw new EmailException("Unable to send email message", e);
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
