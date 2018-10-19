package VMail.Mail;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Transmitor {

    private static SMTP smtp = new SMTP();
    private static SubjectCombiner subjectcombiner = new SubjectCombiner();

    public static boolean TxTransmitor(int state, String content, String ID, InternetAddress[] address) throws IOException, GeneralSecurityException, MessagingException {

        MimeMessage test;
        String subject = null;

        boolean success = false;

        switch (state) {

            case 2:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPreparePrivate(test, address[0], subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 4:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPreparePrivate(test, address[0], subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 5:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPrepareGroup(test, address, subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 6:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPreparePrivate(test, address[0], subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 7:

                test = (MimeMessage) smtp.TxBodyText(content);


                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPreparePrivate(test, address[0], subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 31:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPrepareGroup(test, address, subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 32:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPrepareGroup(test, address, subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 33:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPreparePrivate(test, address[0], subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 34:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPrepareGroup(test, address, subject);

                smtp.TxMail(test);

                success = true;
                break;

            case 35:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPrepareGroup(test, address, subject);

                smtp.TxMail(test);

                success = true;

            case 111:

                test = (MimeMessage) smtp.TxBodyFile(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPrepareGroup(test, address, subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 112:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPrepareGroup(test, address, subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 121:

                test = (MimeMessage) smtp.TxBodyFile(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPreparePrivate(test, address[0], subject);

                smtp.TxMail(test);

                success = true;

                break;

            case 122:

                test = (MimeMessage) smtp.TxBodyText(content);

                subject = subjectcombiner.Combiner(state, ID);

                smtp.TxPreparePrivate(test, address[0], subject);

                smtp.TxMail(test);

                success = true;

                break;

            default:

                break;

        }
        return success;

    }


}
