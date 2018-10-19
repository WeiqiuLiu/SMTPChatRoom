package VMail.Management;

import android.content.ContentValues;
import android.util.Log;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import VMail.Entity.ChatMsgEntity;
import VMail.Entity.Friends;
import VMail.Entity.Groups;
import VMail.Entity.Requests;
import VMail.Entity.settings;

/**
 * Created by willliam on 2018/3/26.
 */

public class Management {

    public static String RandomStringid(int length) {

        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(36);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

    public static void addnewfriends(String name, String address, String specialid) {
        Friends friend1 = new Friends();
        friend1.setName(name);
        friend1.setAddress(address);
        List<Friends> friendlist = DataSupport.findAll(Friends.class);
        if (friendlist.size() != 0) {
            boolean exist = false;
            for (int i = 0; i < friendlist.size(); i++) {
                if (friendlist.get(i).getAddress().equals(address)) {
                    Log.d("check friends", "exist");
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                friend1.setSpecialid(specialid);
                friend1.save();
                Log.d("Friend", "successfully");
            }
        } else {
            friend1.setSpecialid(specialid);
            friend1.save();
        }
    }

    public static void update_req(String address, String id) {
        Log.d("10", address + " " + id);
        List<Requests> rl = DataSupport.where("address == ? and RSid == ?", address, id).find(Requests.class);
        Log.d("5", rl.size() + "");
        ContentValues v = new ContentValues();
        v.put("is_added", "1");
        DataSupport.update(Requests.class, v, rl.get(0).getId());
    }

    public static void addnewrequest(boolean is_friend, String RSID, String address, boolean is_added) {
        Requests newrequest = new Requests(RSID, is_friend, is_added, address);
        newrequest.save();
    }

    public static List<Requests> allrequest() {
        List<Requests> RALL = DataSupport.findAll(Requests.class);
        return RALL;
    }

    public static void addChatMsg(String name, String date, String text, String from_id, boolean isFriend, boolean isOutMsg, String path) {
        ChatMsgEntity msg = new ChatMsgEntity(name, date, text, from_id, isFriend, isOutMsg, path);
        Log.i("26", "" + path);
        msg.save();
    }

    public static List<ChatMsgEntity> getChatMsg(String from_id, boolean isFriend) {
        String b = "0";
        if (isFriend) {
            b = "1";
        }
        Log.d("13", from_id + " " + isFriend);
        List<ChatMsgEntity> list = DataSupport.where("from_id == ? and isFriend == ?", from_id, b).find(ChatMsgEntity.class);
        return list;
    }

    public static ArrayList<String> getAllAddressInGroup(String sid) {
        List<Groups> list = DataSupport.where("Groupsid == ?", sid).find(Groups.class);
        ArrayList<String> sl = new ArrayList<String>();
        for (Groups item : list) {
            sl.add(item.getAddress());
        }
        sl.remove(settings.username);
        return sl;
    }

    public static ArrayList<String> getAllUnblockAddressInGroup(String sid) {
        ArrayList<String> address = new ArrayList<String>();
        List<Groups> newList = DataSupport.where("groupsid == ? and is_blocking == ?", sid, "0").find(Groups.class);
        for (int i = 0; i < newList.size(); i++) {
            address.add(newList.get(i).getAddress());
        }
        address.remove(settings.username);
        return address;
    }


    public static ArrayList<Groups> getGroupsList() {
        List<Groups> alllist = DataSupport.findAll(Groups.class);
        ArrayList<Groups> t = new ArrayList<Groups>();
        ArrayList<String> s = new ArrayList<String>();
        for (Groups item : alllist) {
            if (!s.contains(item.getGroupsid())) {
                s.add(item.getGroupsid());
                Log.d("12", item.getGroupsid());
                t.add(item);
            }
        }
        return t;
    }

    public static List<Groups> findMembersInGroupBySid(String sid) {
        return DataSupport.where("groupsid == ?", sid).find(Groups.class);
    }

    public static String getAddressById(String specialid) {
        List<Friends> newList = DataSupport.where("specialid == ?", specialid).find(Friends.class);
        //Log.d("Friend", newList.get(0).getAddress());
        return newList.get(0).getAddress();
    }

    public static List<Friends> getAllFriends() {
        List<Friends> newList = DataSupport.findAll(Friends.class);
        return newList;
    }

    public static void deletefirends(int id) {
        DataSupport.delete(Friends.class, id);
    }

    public static void clearall() {
        DataSupport.deleteAll(Friends.class);
        DataSupport.deleteAll(Groups.class);
        DataSupport.deleteAll(Requests.class);
        DataSupport.deleteAll(ChatMsgEntity.class);

    }

    public static String addnewcluster(String Groupname) {
        String sid = RandomStringid(6);
        Groups gnew = new Groups();
        Friends Myself = DataSupport.findFirst(Friends.class);
        gnew.setGroupname(Groupname);
        gnew.setGroupsid(sid);
        gnew.setFriendname(Myself.getName());
        gnew.setLeader(true);
        gnew.setAddress(Myself.getAddress());
        gnew.save();
        Log.d("Friend", "OK");
        return sid;
    }

    public static List<Groups> getAllGroups() {
        List<Groups> newList = DataSupport.findAll(Groups.class);
        return newList;
    }

    public static void addnewgroupmember(String groupname, String name, String address, String specialid, boolean role) {
        Groups gnew = new Groups();
        gnew.setGroupsid(specialid);
        gnew.setGroupname(groupname);
        gnew.setFriendname(name);
        gnew.setLeader(role);
        gnew.setAddress(address);
        gnew.save();
        Log.d("Friends", "OKG");
    }

    public static boolean likedByMe(ChatMsgEntity c) {
        List<ChatMsgEntity> cl = DataSupport.where("date == ? and name == ?", c.getDate(), c.getName()).find(ChatMsgEntity.class);
        if (cl.size() > 0) {
            ContentValues values = new ContentValues();
            values.put("likedByMe", "1");
            values.put("num_like", cl.get(0).getNum_like() + 1);
            DataSupport.update(ChatMsgEntity.class, values, cl.get(0).getId());
            return true;
        } else {
            return false;
        }
    }

    public static boolean likedBySomeone(String content, String address, String from_id, boolean isText) {
        List<ChatMsgEntity> cl;
        if (isText) {
            cl = DataSupport.where("text == ? and name == ? and from_id == ?", content, address, from_id).find(ChatMsgEntity.class);
        } else {
            if (!address.equals(settings.username)) {
                cl = DataSupport.where("path == ? and name == ? and from_id == ?", content, address, from_id).find(ChatMsgEntity.class);
            } else {
                cl = DataSupport.where("name == ? and from_id == ?", address, from_id).find(ChatMsgEntity.class);
                Log.d("41", content);
                for (int i = 0; i < cl.size(); i++) {
                    try {
                        if (!cl.get(i).getPath().contains(content)) {
                            cl.remove(i);
                            i--;
                        }
                    } catch (Exception e) {
                        cl.remove(i);
                        i--;
                        Log.d("41", e + "");
                    }
                }
            }
        }
        if (cl.size() > 0) {
            ContentValues values = new ContentValues();
            values.put("num_like", cl.get(0).getNum_like() + 1);
            DataSupport.update(ChatMsgEntity.class, values, cl.get(0).getId());
            return true;
        } else {
            return false;
        }
    }

    public static String searchgroupinf(String specialid) {
        List<Groups> newList = DataSupport.where("groupsid == ?", specialid).find(Groups.class);
        return newList.get(0).getGroupname();
    }

    public static void deletegourpmember(String name, String specialid) {
        List<Groups> newList = DataSupport.where("groupsid == ?", specialid).find(Groups.class);
        for (int i = 0; i < newList.size() && newList.size() != 0; i++) {
            if (newList.get(i).getFriendname().equals(name)) {
                DataSupport.delete(Groups.class, newList.get(i).getId());
                Log.d("Friend", "member deleted");
            } else {
                Log.d("Friend", "member:" + newList.get(i).getFriendname());
            }
        }
        if (newList.size() == 0) {
            Log.d("Friend", "Group not found");
        }
    }

    public static void blockmember(String address, String sid) {
        List<Groups> bm = DataSupport.where("address == ? and groupsid == ?", address, sid).find(Groups.class);
        ContentValues values = new ContentValues();
        values.put("is_blocking", "1");
        DataSupport.update(Groups.class, values, bm.get(0).getId());
    }

    public static void unblockmember(String address, String sid) {
        List<Groups> bm = DataSupport.where("address == ? and groupsid == ?", address, sid).find(Groups.class);
        ContentValues values = new ContentValues();
        values.put("is_blocking", "0");
        DataSupport.update(Groups.class, values, bm.get(0).getId());
    }
}
