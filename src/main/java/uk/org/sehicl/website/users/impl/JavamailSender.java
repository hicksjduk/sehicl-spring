package uk.org.sehicl.website.users.impl;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import uk.org.sehicl.website.users.EmailException;
import uk.org.sehicl.website.users.EmailSender;

public class JavamailSender implements EmailSender
{
    @Override
    public void sendEmail(String subject, String messageText, Addressee... addressees)
            throws EmailException
    {
        final Session mailSession = Session.getDefaultInstance(new Properties(), null);
        final javax.mail.Message message = new MimeMessage(mailSession);
        try
        {
            message.setFrom(new InternetAddress("sehantsicl@gmail.com", "SEHICL Admin"));
            message.setHeader("On-Behalf-Of", "admin@sehicl.org.uk");
            for (Addressee addr : addressees)
            {
                message.addRecipient(RecipientType.TO, getAddress(addr));
            }
            message.setSubject(subject);
            message.setText(messageText);
            Transport.send(message);
        }
        catch (UnsupportedEncodingException | MessagingException e)
        {
            throw new EmailException("Unable to send email message", e);
        }
    }

    private InternetAddress getAddress(Addressee addr) throws EmailException
    {
        InternetAddress answer;
        try
        {
            answer = addr.getName() == null ? new InternetAddress(addr.getAddress())
                    : new InternetAddress(addr.getAddress(), addr.getName());
        }
        catch (AddressException | UnsupportedEncodingException e)
        {
            throw new EmailException("Unable to construct email address", e);
        }
        return answer;
    }
}
