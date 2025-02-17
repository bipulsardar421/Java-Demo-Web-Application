package handler.mailSender_handler;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSenderHandler {

    private final String emailReceipients;
    private final String emailSubject;
    private final String emailBody;

    Session newSession = null;
    MimeMessage mimeMessage = null;

    public MailSenderHandler(String email, String sub, String body) {
        this.emailReceipients = email;
        this.emailSubject = sub;
        this.emailBody = body;
    }

    public void sendEmail() throws MessagingException {
        String fromUser = "mahanbipul260@gmail.com";
        String fromUserPassword = "neazpufsuvjzlctk";
        String emailHost = "smtp.gmail.com";
        Transport transport = newSession.getTransport("smtp");
        transport.connect(emailHost, fromUser, fromUserPassword);
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        transport.close();
        System.out.println("Email successfully sent!!!");
    }

    public MimeMessage draftEmail() throws AddressException, MessagingException, IOException {
        mimeMessage = new MimeMessage(newSession);
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(this.emailReceipients));
        mimeMessage.setSubject(this.emailSubject);
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(this.emailBody, "html/text");
        MimeMultipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(bodyPart);
        mimeMessage.setContent(multiPart);
        return mimeMessage;
    }

    public void setupServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        newSession = Session.getDefaultInstance(properties, null);
    }

}
