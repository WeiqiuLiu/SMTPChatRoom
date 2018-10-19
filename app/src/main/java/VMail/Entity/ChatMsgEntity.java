package VMail.Entity;


import org.litepal.crud.DataSupport;

public class ChatMsgEntity extends DataSupport {

    private int id;

    private String name;

    private String date;

    private String text = null;

    private String from_id;

    private int num_like = 0;

    private boolean isFriend = true;

    private boolean isOutMsg;

    private String path = settings.path;

    private boolean likedByMe = false;

    public ChatMsgEntity() {
    }

    public ChatMsgEntity(String name, String date, String text, boolean isFriend) {
        this.name = name;
        this.date = date;
        this.text = text;
        this.isFriend = isFriend;
    }

    public ChatMsgEntity(String name, String date, boolean isFriend, String path) {
        this.name = name;
        this.date = date;
        this.path = path;
        this.isFriend = isFriend;
    }

    public ChatMsgEntity(String name, String date, String text, String from_id, boolean isFriend, boolean isOutMsg, String path) {
        this.name = name;
        this.date = date;
        this.text = text;
        this.from_id = from_id;
        this.isFriend = isFriend;
        this.path = path;
        this.isOutMsg = isOutMsg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLikedByMe() {
        return likedByMe;
    }

    public void setLikedByMe(boolean likedByMe) {
        this.likedByMe = likedByMe;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNum_like() {
        return num_like;
    }

    public void setNum_like(int num_like) {
        this.num_like = num_like;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isOutMsg() {
        return isOutMsg;
    }

    public void setOutMsg(boolean outMsg) {
        isOutMsg = outMsg;
    }
}