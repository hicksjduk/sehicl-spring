package uk.org.sehicl.website.users.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import uk.org.sehicl.website.users.EmailException;
import uk.org.sehicl.website.users.EmailSender;

public class LocalFileSender implements EmailSender
{

    @Override
    public void sendEmail(String subject, String messageText, Addressee... addressees)
            throws EmailException
    {
        try (PrintWriter pw = new PrintWriter(
                new FileWriter(new File(System.getProperty("user.home"), "email.html"))))
        {
            pw.println(messageText);
        }
        catch (IOException ex)
        {
            throw new EmailException("Unable to generate email file", ex);
        }
    }

}
