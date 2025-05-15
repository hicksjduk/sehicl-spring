package uk.org.sehicl.website.users.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

import uk.org.sehicl.website.EnvVar;
import uk.org.sehicl.website.users.EmailException;
import uk.org.sehicl.website.users.EmailSender;

public class MailgunSender implements EmailSender
{
    private static final Logger LOG = LoggerFactory.getLogger(MailgunSender.class);

    @Value("${MAILGUN_API_KEY:}")
    private String mailgunApiKey;

    @Value("${MAILGUN_DOMAIN:}")
    private String mailgunDomain;

    private final MailgunMessagesApi mailgunMessagesApi = MailgunClient
            .config(mailgunApiKey)
            .createApi(MailgunMessagesApi.class);

    @Override
    public void sendEmail(String subject, String messageText, Addressee... addressees)
            throws EmailException
    {
        var msg = Message
                .builder()
                .from("SEHICL Admin <admin@sehicl.org.uk")
                .to(Stream.of(addressees).map(this::getEmail).toList())
                .subject(subject)
                .text(messageText)
                .build();
        LOG.info("Sending mail: {} to {}", subject, addressees);
        var resp = mailgunMessagesApi.sendMessage(mailgunDomain, msg).getMessage();
        LOG.info("Response message was: {}", resp);
    }

    private String getEmail(Addressee addr)
    {
        return "%s <%s>".formatted(addr.getName(), addr.getAddress());
    }
}
