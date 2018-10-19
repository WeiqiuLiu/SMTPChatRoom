package VMail.Entity;


import org.litepal.crud.DataSupport;

/**
 * Created by willliam on 2018/3/26.
 */

public class Groups extends DataSupport {

    String groupsid;
    boolean isLeader = false;
    String groupname;
    String friendname;
    String address;
    boolean is_blocking = false;
    private int id;

    public boolean isIs_blocking() {
        return is_blocking;
    }

    public void setIs_blocking(boolean is_blocking) {
        this.is_blocking = is_blocking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupsid() {
        return groupsid;
    }

    public void setGroupsid(String groupsid) {
        this.groupsid = groupsid;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
