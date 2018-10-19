package VMail.Mail;

import android.util.Log;

import VMail.Encryption.BASE64Util;
import VMail.Entity.settings;
import VMail.R;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.util.MailSSLSocketFactory;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;


public class IMAP {


    public static IMAPFolder imapFolder;
    public static String content;

    private static Properties IMAPProperties() throws IOException, GeneralSecurityException {

        Properties temp = new Properties();

        InputStream propsfile = settings.context.getResources().openRawResource(R.raw.imapproperties);
        temp.load(propsfile);

        MailSSLSocketFactory ssl = new MailSSLSocketFactory();
        ssl.setTrustAllHosts(true);

        temp.put("mail.imaps.ssl.socketFactory", ssl);
        temp.put("mail.smtp.from", settings.username);

        return temp;

    }

    private static Session IMAPSession() throws IOException, GeneralSecurityException {

        Session temp = Session.getInstance(IMAPProperties(), new MyAuthenricator(settings.username, settings.password));

        return temp;

    }

    private static Store IMAPStore() throws NoSuchProviderException, IOException, GeneralSecurityException {

        Store temp = IMAPSession().getStore();

        return temp;

    }

    public static Store IMAPConnect() throws NoSuchProviderException, MessagingException, IOException, GeneralSecurityException {

        Store temp = IMAPStore();

        temp.connect();

        return temp;

    }

    public static String IMAPTextReader(Message message) throws MessagingException, IOException {

        String contentType = message.getContentType();
        Log.d("3", "" + message.getContentType());

        if (!contentType.startsWith("TEXT/PLAIN")) {

            return null;

        }

        String content = (String) IOUtils.toString(message.getInputStream(), "us-ascii");
        Log.d("3", "" + "----" + content);

        content = BASE64Util.getInstance().decode(content);
        Log.d("3", "" + "---1-" + content);
        if (content.indexOf("ÿ") != -1) {
            content = content.substring(0, content.indexOf("ÿ"));
        }
        Log.d("3", "" + "---3-" + content);
        content = content.substring(0, content.length());
        Log.d("3", "" + "---4-" + content);


        return content;

    }

    public static boolean login() {
        try {
            Store temp = IMAPStore();
            temp.connect();
            temp.close();
            return true;
        } catch (Exception e) {
            Log.d("1", "" + e);
            return false;
        }
    }

    public static String IMAPFileReader(Message message) throws MessagingException, IOException {

        String path = new String();
        String contentType = message.getContentType();

        if (!contentType.startsWith("multipart/MIXED")) {

            return null;

        }

        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        Multipart multiPart = (Multipart) message.getContent();
        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(0);

        String fileName = part.getFileName();

        path = settings.path + File.separator + fileName;
        Log.d("26", path);
        part.saveFile(path);

        return path;

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
