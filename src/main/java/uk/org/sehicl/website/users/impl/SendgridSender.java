package uk.org.sehicl.website.users.impl;

import java.io.IOException;
import java.util.stream.Stream;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

import uk.org.sehicl.website.users.EmailException;
import uk.org.sehicl.website.users.EmailSender;

public class SendgridSender implements EmailSender
{
    @Override
    public void sendEmail(String subject, String messageText, Addressee... addressees)
            throws EmailException
    {
        Email from = new Email("admin@sehicl.org.uk", "SEHICL Admin");
        Content content = new Content("text/plain", messageText);
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
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
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
