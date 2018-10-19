package VMail.Mail;

public class SubjectCombiner {

    private static final String SUBJECT_START = "---";

    //	SUBJECT_COMMAND
    private static final String SUBJECT_COMMAND_MESSAGE = "MES";
    private static final String SUBJECT_COMMAND_NEWFRIEND = "NFR";
    private static final String SUBJECT_COMMAND_GROUP = "GRP";
    private static final String SUBJECT_COMMAND_REJECT = "REJ";
    private static final String SUBJECT_COMMAND_LIKE = "LIK";
    private static final String SUBJECT_COMMAND_ACK_FRIEND = "ANF";
    private static final String SUBJECT_COMMAND_ACK_GROUP = "AGP";
    private static final int SUBJECT_NEWFRIEND = 2;
    private static final int SUBJECT_REJECT = 4;
    private static final int SUBJECT_LIKE = 5;
    private static final int SUBJECT_ACK_FRIEND = 6;
    private static final int SUBJECT_ACK_GROUP = 7;

    //	SUBJECT_COMMUNICATE_TYPE
    private static final String SUBJECT_COMMUNICATE_GROUP = "CGP";
    private static final String SUBJECT_COMMUNICATE_PRIVATE = "CFR";

    //	SUBJECT_MESSAGE_TYPE
    private static final String SUBJECT_MESSAGE_FILE = "FIL";
    private static final String SUBJECT_MESSAGE_TEXT = "TXT";

    private static final int SUBJECT_FILE_GROUP = 111;
    private static final int SUBJECT_FILE_PRIVATE = 121;
    private static final int SUBJECT_TEXT_GROUP = 112;
    private static final int SUBJECT_TEXT_PRIVATE = 122;

    //	SUBJECT_COMMAND_GROUP
    private static final String SUBJECT_GROUP_NEW_GROUP = "NGP";
    private static final String SUBJECT_GROUP_NEW_MEMBERS = "NMB";
    private static final String SUBJECT_GROUP_NEW_MEMBERS_REQ = "NMR";
    private static final String SUBJECT_GROUP_DELETE_MEMBERS = "GDL";
    private static final String SUBJECT_GROUP_NEW_MEMBERS_INV = "INV";

    private static final int SUBJECT_NEW_GROUP = 31;
    private static final int SUBJECT_NEW_MEMBERS = 32;
    private static final int SUBJECT_NEW_MEMBERS_REQ = 33;
    private static final int SUBJECT_DELETE_MEMBERS = 34;
    private static final int SUBJECT_NEW_MEMBERS_INV = 35;

// SUBJECT_START + SUBJECT_COMMAND + SUBJECT_COMMUNICATION_TYPE + SUBJECT_MESSAGE_TYPE + SUBJECT_ID
// SUBJECT_START + SUBJECT_COMMAND + (SUBJECT_ID)
// SUBJECT_START + SUBJECT_COMMAND + SUBJECT_COMMAND_GROUP + (SUBJECT_ID)

    public static String Combiner(int state, String ID) {

        String subject = new String();
        subject = null;

        switch (state) {

            case SUBJECT_FILE_GROUP:

                subject = SUBJECT_START + SUBJECT_COMMAND_MESSAGE + SUBJECT_COMMUNICATE_GROUP + SUBJECT_MESSAGE_FILE + ID;
                return subject;

            case SUBJECT_TEXT_GROUP:

                subject = SUBJECT_START + SUBJECT_COMMAND_MESSAGE + SUBJECT_COMMUNICATE_GROUP + SUBJECT_MESSAGE_TEXT + ID;
                return subject;

            case SUBJECT_FILE_PRIVATE:

                subject = SUBJECT_START + SUBJECT_COMMAND_MESSAGE + SUBJECT_COMMUNICATE_PRIVATE + SUBJECT_MESSAGE_FILE + ID;
                return subject;

            case SUBJECT_TEXT_PRIVATE:

                subject = SUBJECT_START + SUBJECT_COMMAND_MESSAGE + SUBJECT_COMMUNICATE_PRIVATE + SUBJECT_MESSAGE_TEXT + ID;
                return subject;

            case SUBJECT_NEWFRIEND:

                subject = SUBJECT_START + SUBJECT_COMMAND_NEWFRIEND + ID;
                return subject;

            case SUBJECT_REJECT:

                subject = SUBJECT_START + SUBJECT_COMMAND_REJECT + ID;
                return subject;

            case SUBJECT_LIKE:

                subject = SUBJECT_START + SUBJECT_COMMAND_LIKE;
                return subject;

            case SUBJECT_ACK_FRIEND:

                subject = SUBJECT_START + SUBJECT_COMMAND_ACK_FRIEND + ID;
                return subject;

            case SUBJECT_ACK_GROUP:

                subject = SUBJECT_START + SUBJECT_COMMAND_ACK_GROUP + ID;
                return subject;

            case SUBJECT_NEW_GROUP:

                subject = SUBJECT_START + SUBJECT_COMMAND_GROUP + SUBJECT_GROUP_NEW_GROUP + ID;
                return subject;

            case SUBJECT_NEW_MEMBERS:

                subject = SUBJECT_START + SUBJECT_COMMAND_GROUP + SUBJECT_GROUP_NEW_MEMBERS + ID;
                return subject;

            case SUBJECT_NEW_MEMBERS_REQ:

                subject = SUBJECT_START + SUBJECT_COMMAND_GROUP + SUBJECT_GROUP_NEW_MEMBERS_REQ + ID;
                return subject;

            case SUBJECT_DELETE_MEMBERS:

                subject = SUBJECT_START + SUBJECT_COMMAND_GROUP + SUBJECT_GROUP_DELETE_MEMBERS + ID;
                return subject;

            case SUBJECT_NEW_MEMBERS_INV:

                subject = SUBJECT_START + SUBJECT_COMMAND_GROUP + SUBJECT_GROUP_NEW_MEMBERS_INV + ID;
                return subject;

        }

        return subject;

    }

    public static void main(String[] args) {

        System.out.println(Combiner(5, null));

    }

}
