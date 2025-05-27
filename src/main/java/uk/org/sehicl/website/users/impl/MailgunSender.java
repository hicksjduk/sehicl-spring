package uk.org.sehicl.website.users.impl;

import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.util.Constants;

import uk.org.sehicl.website.users.EmailException;
import uk.org.sehicl.website.users.EmailSender;

public class MailgunSender implements EmailSender
{
    private static final Logger LOG = LoggerFactory.getLogger(MailgunSender.class);

    private final String mailgunDomain;

    private final MailgunMessagesApi mailgunMessagesApi;

    public MailgunSender(String mailgunApiKey, String mailgunDomain)
    {
        this.mailgunDomain = mailgunDomain;
        mailgunMessagesApi = MailgunClient
                .config(Constants.EU_REGION_BASE_URL, mailgunApiKey)
                .createApi(MailgunMessagesApi.class);
    }

    @Override
    public void sendEmail(String subject, String messageText, Addressee... addressees)
            throws EmailException
    {
        var msg = Message
                .builder()
                .headers(Map.of("Content-Type", "text/html"))
                .from(Addressee
                        .withAddress("admin@sehicl.org.uk")
                        .withName("SEHICL Admin")
                        .toString())
                .to(Stream.of(addressees).map(Addressee::toString).toList())
                .subject(subject)
                .text(messageText)
                .build();
        LOG.info("Sending mail: {} to {}", subject, addressees);
        var resp = mailgunMessagesApi.sendMessage(mailgunDomain, msg).getMessage();
        LOG.info("Response message was: {}", resp);
    }
}
