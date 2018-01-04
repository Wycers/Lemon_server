package webserver;
public class item {
    private int uid, type;
    private String username, name, avatar;
    item(int uid, int type, String username, String name, String avatar) {
        this.uid = uid;
        this.type = type;
        this.username = username;
        this.name = name;
        this.avatar = avatar;
    }
    item(int uid, int type, String username, String name) {
        this.uid = uid;
        this.type = type;
        this.username = username;
        this.name = name;
    }
    item(User p) {
        this.uid = p.getUid();
        this.type = p.getType();
        this.username = p.getUsername();
        this.name = p.getName();
        this.avatar = p.getAvatar();
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
    /**
     * @return the uid
     */
    public int getUid() {
        return uid;
    }
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }
}