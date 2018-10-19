package VMail.Entity;

import org.litepal.crud.DataSupport;


public class Friends extends DataSupport {

    private int id;

    private String specialid;

    private String name;

    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecialid() {
        return specialid;
    }

    public void setSpecialid(String specialid) {
        this.specialid = specialid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
