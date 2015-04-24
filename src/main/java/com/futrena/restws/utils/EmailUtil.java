package com.futrena.restws.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;

public class EmailUtil
{
    @Autowired
    private JavaMailSender mailSender;
    
    public void execute(List<HashMap<String, Object>> reportStats)
    {
        // TODO: Construct the email with velocity
        StringBuilder emailBody = new StringBuilder();

        emailBody.append("<html><body>");
        emailBody.append("<p>");
        emailBody.append("The unsubscription job started.");
        emailBody.append("</p>");
        emailBody.append("<p>");
        emailBody.append("<h3>Files processed succesfully</h3>");
        emailBody.append("<ul>");
        emailBody.append("</ul>");
        emailBody.append("</p>");
        emailBody.append("<p>");
        emailBody.append("<h3>Files processed with error</h3>");
        emailBody.append("<ul>");

        emailBody.append("</ul>");
        emailBody.append("</p>");
        emailBody.append("<p>");
        emailBody.append("<h3>Processed User Stats</h3>");
        emailBody.append("<ul>");
        emailBody.append("</ul>");
        emailBody.append("</p>");
        emailBody.append("</body></html>");
        sendEmailInternal("rcprestige@live.com", "rcprestige@live.com", "Test",
                emailBody.toString());
    }



    private void sendEmailInternal(final String toEmailAddresses, final String fromEmailAddress, final String subject, final String body)
    {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception
            {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
                message.setTo(toEmailAddresses);
                message.setFrom(new InternetAddress(fromEmailAddress));
                message.setSubject(subject);

                message.setText(body, true);
            }
        };
        try
        {
            this.mailSender.send(preparator);
        }
        catch (MailSendException ex)
        {

        }

    }
}
