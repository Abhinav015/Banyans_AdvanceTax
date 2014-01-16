package helper;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailAPI {

    public SendMailAPI() {
    }
    private String HOST_NAME = "smtp.mail.yahoo.com";
    String messageBody;

    public void postMail(String recipients[], String subject, String message,
            String from, String emailPassword, String sign, String manager, String salutation, String taxDataTabular) throws MessagingException {

        boolean debug = false;
        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.timeout", "30000");

        Authenticator authenticator = new SMTPAuthenticator(from, emailPassword);
        Session session = Session.getDefaultInstance(props, authenticator);
        session.setDebug(debug);

        Message msg = new MimeMessage(session);

        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        message = "<font face='Calibri' style='font-size:10pt;'>" + salutation + ", <br><br>" + message + "<br>" + taxDataTabular + "<br><br>Regards<br>" + manager + "<br>" + "<br></font>";
        String details = sign;
        message = message + details;
        msg.setSubject(subject);
        msg.setContent(message, "text/html");

        Transport.send(msg);
        System.out.println("Sucessfully Sent mail to All Users");

    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        String username;
        String password;

        private SMTPAuthenticator(String authenticationUser, String authenticationPassword) {
            username = authenticationUser;
            password = authenticationPassword;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication(username, password);
        }
    }
}
