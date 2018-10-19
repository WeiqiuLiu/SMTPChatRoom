package VMail.Entity;

import org.litepal.crud.DataSupport;

/**
 * Created by willliam on 2018/3/28.
 */

public class Requests extends DataSupport {

    int id;

    String RSid;

    boolean is_friend;

    boolean is_added;

    String address;

    public Requests() {
    }

    public Requests(String RSid, boolean is_friend, boolean is_added, String address) {
        this.RSid = RSid;
        this.is_friend = is_friend;
        this.is_added = is_added;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRSid() {
        return RSid;
    }

    public void setRSid(String RSid) {
        this.RSid = RSid;
    }

    public boolean isIs_friend() {
        return is_friend;
    }

    public void setIs_friend(boolean is_friend) {
        this.is_friend = is_friend;
    }

    public boolean isIs_added() {
        return is_added;
    }

    public void setIs_added(boolean is_added) {
        this.is_added = is_added;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}