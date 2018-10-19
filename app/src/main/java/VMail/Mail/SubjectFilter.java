package VMail.Mail;

public class SubjectFilter {

    private static final String SUBJECT_START = "---";
    private static final int SUBJECT_START_LENGTH = 3;

    //	SUBJECT_COMMAND
    private static final String SUBJECT_COMMAND_MESSAGE = "MES";
    private static final String SUBJECT_COMMAND_NEWFRIEND = "NFR";
    private static final String SUBJECT_COMMAND_GROUP = "GRP";
    private static final String SUBJECT_COMMAND_REJECT = "REJ";
    private static final String SUBJECT_COMMAND_LIKE = "LIK";
    private static final String SUBJECT_COMMAND_ACK_FRIEND = "ANF";
    private static final String SUBJECT_COMMAND_ACK_GROUP = "AGP";
    private static final int SUBJECT_COMMAND_LENGTH = 3;
    private static final int SUBJECT_NEWFRIEND = 2;
    private static final int SUBJECT_REJECT = 4;
    private static final int SUBJECT_LIKE = 5;
    private static final int SUBJECT_ACK_FRIEND = 6;
    private static final int SUBJECT_ACK_GROUP = 7;

    //	SUBJECT_COMMUNICATE_TYPE
    private static final String SUBJECT_COMMUNICATE_GROUP = "CGP";
    private static final String SUBJECT_COMMUNICATE_PRIVATE = "CFR";
    private static final int SUBJECT_COMMUNICATE_LENGTH = 3;

    //	SUBJECT_MESSAGE_TYPE
    private static final String SUBJECT_MESSAGE_FILE = "FIL";
    private static final String SUBJECT_MESSAGE_TEXT = "TXT";
    private static final int SUBJECT_MESSAGE_LENGTH = 3;

    //	SUBJECT_COMMAND_GROUP
    private static final String SUBJECT_GROUP_NEW_GROUP = "NGP";
    private static final String SUBJECT_GROUP_NEW_MEMBERS = "NMB";
    private static final String SUBJECT_GROUP_NEW_MEMBERS_REQ = "NMR";
    private static final String SUBJECT_GROUP_DELETE_MEMBERS = "GDL";
    private static final String SUBJECT_GROUP_NEW_MEMBERS_INV = "INV";
    private static final int SUBJECT_GROUP_LENGTH = 3;

    private static final int SUBJECT_NEW_GROUP = 31;
    private static final int SUBJECT_NEW_MEMBERS = 32;
    private static final int SUBJECT_NEW_MEMBERS_REQ = 33;
    private static final int SUBJECT_DELETE_MEMBERS = 34;
    private static final int SUBJECT_NEW_MEMBERS_INV = 35;

// SUBJECT_START + SUBJECT_COMMAND + SUBJECT_COMMUNICATION_TYPE + SUBJECT_MESSAGE_TYPE + SUBJECT_ID
// SUBJECT_START + SUBJECT_COMMAND + (SUBJECT_ID)
// SUBJECT_START + SUBJECT_COMMAND + SUBJECT_COMMAND_GROUP + (SUBJECT_ID)

    public static int Filter(String subject) {

        int state = -1;

        if (subject.length() >= 6) {

            switch (subject.substring(0, SUBJECT_START_LENGTH)) {

                case SUBJECT_START:

                    state = 0;
                    break;

                default:

                    return state;

            }

        } else {

            return state;

        }

        switch (subject.substring(SUBJECT_START_LENGTH, (SUBJECT_START_LENGTH + SUBJECT_COMMAND_LENGTH))) {

            case SUBJECT_COMMAND_MESSAGE:

                state = 1;
                break;

            case SUBJECT_COMMAND_NEWFRIEND:

                state = SUBJECT_NEWFRIEND;
                return state;

            case SUBJECT_COMMAND_GROUP:

                state = 3;
                break;

            case SUBJECT_COMMAND_REJECT:

                state = SUBJECT_REJECT;
                return state;

            case SUBJECT_COMMAND_LIKE:

                state = SUBJECT_LIKE;
                return state;

            case SUBJECT_COMMAND_ACK_FRIEND:

                state = SUBJECT_ACK_FRIEND;
                return state;

            case SUBJECT_COMMAND_ACK_GROUP:

                state = SUBJECT_ACK_GROUP;
                return state;

            default:

                return state;

        }

        switch (state) {

            case 1:

                if (subject.length() == 18) {

                    switch (subject.substring((SUBJECT_START_LENGTH + SUBJECT_COMMAND_LENGTH),
                            (SUBJECT_START_LENGTH + SUBJECT_COMMAND_LENGTH + SUBJECT_COMMUNICATE_LENGTH))) {

                        case SUBJECT_COMMUNICATE_GROUP:

                            state = state * 10 + 1;
                            break;

                        case SUBJECT_COMMUNICATE_PRIVATE:

                            state = state * 10 + 2;
                            break;

                        default:

                            state = 10;
                            return state;

                    }

                } else {

                    state = 10;
                    return 10;

                }


                switch (subject.substring((SUBJECT_START_LENGTH + SUBJECT_COMMAND_LENGTH + SUBJECT_COMMUNICATE_LENGTH),
                        (SUBJECT_START_LENGTH + SUBJECT_COMMAND_LENGTH + SUBJECT_COMMUNICATE_LENGTH + SUBJECT_MESSAGE_LENGTH))) {

                    case SUBJECT_MESSAGE_FILE:

                        state = state * 10 + 1;
                        return state;

                    case SUBJECT_MESSAGE_TEXT:

                        state = state * 10 + 2;
                        return state;

                    default:

                        state = 100;
                        return state;

                }

            case 3:

                if (subject.length() == 15) {

                    switch (subject.substring((SUBJECT_START_LENGTH + SUBJECT_COMMAND_LENGTH),
                            (SUBJECT_START_LENGTH + SUBJECT_COMMAND_LENGTH + SUBJECT_GROUP_LENGTH))) {

                        case SUBJECT_GROUP_NEW_GROUP:

                            state = SUBJECT_NEW_GROUP;
                            return state;

                        case SUBJECT_GROUP_NEW_MEMBERS:

                            state = SUBJECT_NEW_MEMBERS;
                            return state;

                        case SUBJECT_GROUP_NEW_MEMBERS_REQ:

                            state = SUBJECT_NEW_MEMBERS_REQ;
                            return state;

                        case SUBJECT_GROUP_DELETE_MEMBERS:

                            state = SUBJECT_DELETE_MEMBERS;
                            return state;

                        case SUBJECT_GROUP_NEW_MEMBERS_INV:

                            state = SUBJECT_NEW_MEMBERS_INV;
                            return state;

                        default:

                            state = 30;
                            return state;

                    }

                } else {

                    state = 30;
                    return 30;

                }

        }

        state = -2;
        return state;

    }

    public static String returnIDCommunicate(String subject) {

        return subject.substring(12);

    }

    public static String returnIDCommand(String subject) {

        return subject.substring(6);

    }

    public static String returnIDGroup(String subject) {

        return subject.substring(9);

    }
	
	
	/*public static void main(String[] args) {
		
		String subject = "---MESCGPTXT123456";
		System.out.println(Filter(subject));
		System.out.println(returnIDCommunicate(subject));
		
	}*/

}
