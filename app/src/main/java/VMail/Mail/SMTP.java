package VMail.Mail;

import VMail.Encryption.BASE64Util;
import VMail.Entity.settings;
import VMail.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SMTP {

    public SMTP() {
    }

    private static Properties SMTPProperties() throws IOException, GeneralSecurityException {

        Properties temp = new Properties();

        InputStream propsfile = settings.context.getResources().openRawResource(R.raw.smtpproperties);
        temp.load(propsfile);

        temp.put("mail.smtp.from", settings.username);


        return temp;

    }

    private static Session SMTPSession() throws IOException, GeneralSecurityException {

        Session temp = Session.getInstance(SMTPProperties(), new MyAuthenricator(settings.username, settings.password));

        return temp;

    }

    public static Message TxBodyText(String text) throws IOException, GeneralSecurityException, MessagingException {

        Message message = new MimeMessage(SMTPSession());

        text = BASE64Util.getInstance().encode(text);
        message.setText(text);
        message.saveChanges();

        return message;

    }

    public static Message TxBodyFile(String path) throws IOException, GeneralSecurityException, MessagingException {

        Message message = new MimeMessage(SMTPSession());
        MimeBodyPart attach = new MimeBodyPart();
        MimeMultipart body = new MimeMultipart();

        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

        DataSource ds = new FileDataSource(path);
        DataHandler dh = new DataHandler(ds);
        String[] s = path.split("/");
        attach.setFileName(MimeUtility.encodeText(s[s.length - 1]));
        attach.setDataHandler(dh);

        body.addBodyPart(attach);
        message.setContent(body);
        message.saveChanges();

        return message;

    }

    public static void TxPrepareGroup(Message item, InternetAddress[] addresses, String subject) throws MessagingException {

        item.setRecipients(Message.RecipientType.TO, addresses);
        item.setSubject(subject);

    }

    public static void TxPreparePrivate(Message item, InternetAddress address, String subject) throws MessagingException {

        item.setRecipient(Message.RecipientType.TO, address);
        item.setSubject(subject);

    }

    public static void TxMail(Message item) throws IOException, GeneralSecurityException, MessagingException {

        Transport Tx = SMTPSession().getTransport();

        Tx.connect();

        Tx.sendMessage(item, item.getAllRecipients());

        Tx.close();

    }

    static class MyAuthenricator extends Authenticator {

        String user = null;
        String pswd = "";

        public MyAuthenricator(String user, String pswd) {

            this.user = user;
            this.pswd = pswd;

        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication(user, pswd);

        }

    }

}
