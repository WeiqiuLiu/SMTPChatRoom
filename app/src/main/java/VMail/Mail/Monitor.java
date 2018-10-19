package VMail.Mail;


import android.util.Log;

import com.sun.mail.imap.IMAPFolder;

import java.io.File;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import VMail.Activity.AddContactActivity;
import VMail.Activity.ChatActivity;
import VMail.Activity.MainActivity;
import VMail.Entity.settings;
import VMail.Management.Management;


public class Monitor {

    private static final long KEEP_ALIVE_FREQ = 5000;
//    private static final String IRIDIUM_MAILBOX_PROCESSED="Processed";
//    private static final String IRIDIUM_MAILBOX_INVALID="Invalid";

    private static IMAP imap = new IMAP();
    private static IMAPFolder imapFolder;
    private static Store store;
    //    private IMAPFolder processedFolder;
//    private IMAPFolder invalidFolder;
    private static boolean FLAG = false;
    private static SubjectFilter subjectfilter = new SubjectFilter();


    public static void monitor() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        FLAG = false;

                        Log.d("2", "111");
                        while (true) {
                            if (settings.stop) {
                                break;
                            }
                            if (FLAG) {
                                store = IMAP.IMAPConnect();
                                FLAG = false;

                                int count = imapFolder.getMessageCount();
                                int start = 0;
                                int stop = 0;
                                if (count > 50) {
                                    start = count - 49;
                                    stop = count;
                                } else {
                                    start = 1;
                                    stop = count;
                                }
                                Message[] messages = imapFolder.getMessages(start, stop);

                                for (Message message : messages) {

                                    if (!message.getFlags().contains(Flags.Flag.SEEN) && message.getFlags().contains(Flags.Flag.FLAGGED)) {
                                        message.setFlag(Flags.Flag.SEEN, true);
                                        String subject = null;
                                        subject = message.getSubject();
                                        int state = -1;
                                        state = SubjectFilter.Filter(subject);

									/*if (state == -1 || state == -2 || state == 10 || state == 100 || state == 30) {

										message.setFlag(Flags.Flag.FLAGGED, true);

									}*/

                                        if (state == 111 || state == 112 || state == 121 || state == 122) {

                                            String ID = null;
                                            ID = SubjectFilter.returnIDCommunicate(subject);

                                            boolean FLAG0 = false;

//            					if(ID) {
//
//            						FLAG0 = true;
//
//            					} esle {
//
//                					message.setFlag(Flags.Flag.FLAGGED, true);
//
//            					}
//
                                            FLAG0 = true;
                                            //????? ?????ID????
//            					message.setFlag(Flags.Flag.SEEN, true);

                                            if (FLAG0) {

                                                String temp;
                                                Log.d("3", "" + state + "----" + message);
                                                switch (state) {

                                                    case 111:

                                                        temp = IMAP.IMAPFileReader(message);
                                                        android.os.Message msg111 = new android.os.Message();
                                                        String address111 = ((InternetAddress) message.getFrom()[0]).getAddress();
                                                        String id = SubjectFilter.returnIDCommunicate(message.getSubject());
                                                        Management.addChatMsg(address111, settings.getDate(), null, id, false, false, temp);
                                                        temp = address111 + "-!!!-" + temp;
                                                        msg111.obj = temp;
                                                        msg111.what = 111;
                                                        Log.d("14", temp);
                                                        Log.d("14", SubjectFilter.returnIDCommunicate(message.getSubject()));

                                                        if (ChatActivity.h != null) {
//                                                        Log.d("3",""+msg.obj);
//                                                        Log.d("3",""+msg);
                                                            ChatActivity.h.sendMessage(msg111);
                                                        }
                                                        break;

                                                    case 112:

                                                        temp = IMAP.IMAPTextReader(message);
                                                        android.os.Message msg112 = new android.os.Message();
                                                        String address112 = ((InternetAddress) message.getFrom()[0]).getAddress();
                                                        Management.addChatMsg(address112, settings.getDate(), temp, SubjectFilter.returnIDCommunicate(message.getSubject()), false, false, null);
                                                        temp = address112 + "-!!!-" + temp;
                                                        msg112.obj = temp;
                                                        msg112.what = 112;
                                                        Log.d("14", temp);
                                                        Log.d("14", SubjectFilter.returnIDCommunicate(message.getSubject()));

                                                        if (ChatActivity.h != null) {
//                                                        Log.d("3",""+msg.obj);
//                                                        Log.d("3",""+msg);
                                                            ChatActivity.h.sendMessage(msg112);
                                                        }

                                                        break;

                                                    case 121:

                                                        temp = IMAP.IMAPFileReader(message);
                                                        Log.i("26", "" + temp);
                                                        android.os.Message msg121 = new android.os.Message();
                                                        msg121.obj = temp;
                                                        msg121.what = 121;

                                                        Log.i("26", "" + temp);
                                                        Management.addChatMsg(((InternetAddress) message.getFrom()[0]).getAddress(), settings.getDate(), null, SubjectFilter.returnIDCommunicate(message.getSubject()), true, false, temp);

                                                        if (ChatActivity.h != null) {
//                                                        Log.d("3",""+msg.obj);
//                                                        Log.d("3",""+msg);
                                                            ChatActivity.h.sendMessage(msg121);
                                                        }
                                                        break;

                                                    case 122:
                                                        Log.d("3", "" + state + "----" + message);

                                                        temp = IMAP.IMAPTextReader(message);
                                                        Log.i("3", "" + temp);
                                                        android.os.Message msg122 = new android.os.Message();
                                                        msg122.obj = temp;
                                                        msg122.what = 122;

                                                        Management.addChatMsg(((InternetAddress) message.getFrom()[0]).getAddress(), settings.getDate(), temp, SubjectFilter.returnIDCommunicate(message.getSubject()), true, false, null);

                                                        if (ChatActivity.h != null) {
//                                                        Log.d("3",""+msg.obj);
//                                                        Log.d("3",""+msg);
                                                            ChatActivity.h.sendMessage(msg122);
                                                        }

                                                        break;

                                                }

                                            }

                                            message.setFlag(Flags.Flag.FLAGGED, false);
                                        } else if (state == 4) {

                                            if (SubjectFilter.returnIDCommand(subject).length() == 6) {

                                                message.setFlag(Flags.Flag.SEEN, true);

                                                String temp;
                                                temp = "Rejected BY " + message.getFrom().toString() + " " + SubjectFilter.returnIDCommand(subject);

                                            } else {

                                                message.setFlag(Flags.Flag.FLAGGED, true);

                                            }
                                            //?????????

                                        } else if (state == 2) {
                                            if (SubjectFilter.returnIDCommand(subject).length() == 6) {
                                                String address = ((InternetAddress) message.getFrom()[0]).getAddress();

                                                String temp;
                                                temp = "Friend Request FROM " + ((InternetAddress) message.getFrom()[0]).getAddress() + " " + SubjectFilter.returnIDCommand(subject);
                                                //Log.d("21",temp);
                                                Management.addnewrequest(true, SubjectFilter.returnIDCommand(subject), address, false);
                                                android.os.Message msg = new android.os.Message();
                                                msg.obj = temp;
                                                msg.what = 2;
                                                if (AddContactActivity.h != null) {
                                                    AddContactActivity.h.sendMessage(msg);
                                                }

                                            }
                                            message.setFlag(Flags.Flag.FLAGGED, false);

                                        } else if (state == 5) {
                                            String temp = IMAP.IMAPTextReader(message);
                                            String s[] = temp.split("---!!!!---");
                                            if (s[0].equals("true")) {
                                                Management.likedBySomeone(s[1], s[2], s[3], true);
                                            } else {
                                                if (!s[2].equals(settings.username)) {
                                                    Management.likedBySomeone(settings.path + File.separator + s[1], s[2], s[3], false);
                                                } else {
                                                    Management.likedBySomeone(s[1], s[2], s[3], false);
                                                }
                                            }

                                            android.os.Message msg5 = new android.os.Message();
                                            msg5.what = 5;

                                            if (ChatActivity.h != null) {
//                                                        Log.d("3",""+msg.obj);
//                                                        Log.d("3",""+msg);
                                                ChatActivity.h.sendMessage(msg5);
                                            }

                                            message.setFlag(Flags.Flag.FLAGGED, false);
                                        } else if (state == 6) {

                                            if (SubjectFilter.returnIDCommand(subject).length() == 6) {//&& IMAP.IMAPTextReader(message).length() == 6) {
                                                Management.addnewfriends(((InternetAddress) message.getFrom()[0]).getAddress(), ((InternetAddress) message.getFrom()[0]).getAddress(), SubjectFilter.returnIDCommand(subject));
                                                if (MainActivity.h != null) {
                                                    MainActivity.h.sendEmptyMessage(6);
                                                }
                                            }
                                            //??ACK;

                                            message.setFlag(Flags.Flag.FLAGGED, false);
                                        } else if (state == 7) {

                                            if (SubjectFilter.returnIDCommand(subject).length() == 6) {


                                                //String temp;
                                                //temp = "Group ACK " + ((InternetAddress) message.getFrom()[0]).getAddress() + " " + SubjectFilter.returnIDCommand(subject);
                                                String id = SubjectFilter.returnIDCommand(subject);
                                                String[] content = IMAP.IMAPTextReader(message).split(id);
                                                String groupname = content[1];
                                                //Transmitor.TxTransmitor(7,)
                                                Management.addnewgroupmember(groupname, ((InternetAddress) message.getFrom()[0]).getAddress(), ((InternetAddress) message.getFrom()[0]).getAddress(), id, false);
                                                ArrayList<String> sl = Management.getAllAddressInGroup(id);
                                                InternetAddress[] ia = new InternetAddress[sl.size()];
                                                if (ia.length > 0) {
                                                    for (int i = 0; i < sl.size(); i++) {
                                                        ia[i] = new InternetAddress(sl.get(i));
                                                    }
                                                    Transmitor.TxTransmitor(31, "AckAckAckAckAckAckAck", id, ia);
                                                }
                                                Log.d("8", groupname + "," + ((InternetAddress) message.getFrom()[0]).getAddress() + "," + ((InternetAddress) message.getFrom()[0]).getAddress() + "," + id);
                                            }
                                            message.setFlag(Flags.Flag.FLAGGED, false);
                                            //?ACK

                                        } else if (state == 31 || state == 32 || state == 33 || state == 34) {
                                            Log.d("7", state + " " + IMAP.IMAPTextReader(message).length());

                                            if (IMAP.IMAPTextReader(message).length() != 0) {

                                                message.setFlag(Flags.Flag.SEEN, true);

                                                Log.d("7", state + "");

                                                String temp;

                                                switch (state) {

                                                    case 31:
                                                        Address[] a = message.getAllRecipients();
                                                        String id = SubjectFilter.returnIDGroup(subject);
                                                        //Log.d("20",Management.searchgroupinf(id)+" "+a.length+" "+a[0].toString()+" "+IMAP.IMAPTextReader(message));
                                                        //temp = "NEW Group " + SubjectFilter.returnIDGroup(subject);
                                                        if (!IMAP.IMAPTextReader(message).contains("AckAckAckAckAckAckAck")) {
                                                            Log.d("20", " " + id);
                                                            String address = ((InternetAddress) message.getFrom()[0]).getAddress() + ";";
                                                            for (int i = 0; i < a.length; i++) {
                                                                address += a[i].toString() + ";";
                                                            }
                                                            address += IMAP.IMAPTextReader(message) + ";";
                                                            temp = address + "--" + id;

                                                            Log.d("7", temp);
                                                            Log.d("10", address);

                                                            Management.addnewrequest(false, id, address, false);
                                                            android.os.Message msg = new android.os.Message();
                                                            msg.obj = temp;
                                                            msg.what = 31;
                                                            if (AddContactActivity.h != null) {
                                                                AddContactActivity.h.sendMessage(msg);
                                                            }
                                                        } else {
                                                            Log.d("20", " " + id);
                                                            ArrayList<String> sl = Management.getAllAddressInGroup(id);
                                                            Log.d("20", sl.size() + " " + id);
                                                            for (int i = 0; i < a.length; i++) {
                                                                Log.d("20", Management.searchgroupinf(id) + " " + a[i].toString());
                                                                if (!sl.contains(a[i].toString()) && !a[i].toString().equals(settings.username)) {
                                                                    Log.d("20", Management.searchgroupinf(id) + " " + a[i].toString());
                                                                    Management.addnewgroupmember(Management.searchgroupinf(id), a[i].toString(), a[i].toString(), id, false);
                                                                }
                                                            }
                                                        }
                                                        break;

                                                    case 32:

                                                        temp = "NEW MEMBER " + SubjectFilter.returnIDGroup(subject);
                                                        break;

                                                    case 33:

                                                        temp = "NEW MEMBER REQ" + SubjectFilter.returnIDGroup(subject);
                                                        break;

                                                    case 34:

                                                        temp = "DELETE MEMBER " + SubjectFilter.returnIDGroup(subject);
                                                        break;

                                                    case 35:

                                                        temp = "NEW Group INV " + SubjectFilter.returnIDGroup(subject);
                                                        break;

                                                }

                                                message.setFlag(Flags.Flag.FLAGGED, false);

                                            } else {

                                                message.setFlag(Flags.Flag.FLAGGED, true);

                                            }

                                        }

                                    }

                                }

                                store.close();

                            } else {

                                store = IMAP.IMAPConnect();
                                imapFolder = (IMAPFolder) store.getFolder("INBOX");
                                imapFolder.open(Folder.READ_WRITE);
                                int count = imapFolder.getMessageCount();
                                int start = 0;
                                int stop = 0;
                                if (count > 50) {
                                    start = count - 49;
                                    stop = count;
                                } else {
                                    start = 1;
                                    stop = count;
                                }
                                Message[] messages = imapFolder.getMessages(start, stop);

                                for (Message message : messages) {

                                    if (!message.getFlags().contains(Flags.Flag.SEEN)) {
                                        int state = SubjectFilter.Filter(message.getSubject());
                                        if (state == -1 || state == -2 || state == 10 || state == 100 || state == 30) {

                                        } else {
                                            FLAG = true;
                                            message.setFlag(Flags.Flag.FLAGGED, true);
                                        }
                                    }
                                }
                                Log.i("1", "" + FLAG);
                                if (!FLAG) {
                                    Thread.currentThread().sleep(KEEP_ALIVE_FREQ);
                                }
                            }
                        }
                        break;
                    } catch (Exception e) {
                        Log.d("err", "" + e);
                    }
                }
            }

        }).start();
    }

}
