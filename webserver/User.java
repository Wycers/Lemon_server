package webserver;

public class User {
    private int uid, type;
    private String username, name, password, avatar;
    public User(int uid_, String username_, String name_, String password_, int type_) {
        this.uid = uid_;
        this.username = username_;
        this.name = name_;
        this.password = password_;
        this.type = type_;
        this.avatar = "/static/1.jpg";
    }
    public String toString () {
        return String.format("%d", uid);
    }
    public int getUid() {
        return this.uid;
    }
    public int getType() {
        return this.type;
    }
    public String getPassword() {
        return this.password;
    }
    public String getUsername() {
        return this.username;
    }
    public String getName() {
        return this.name;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    } 
    public void setType(int type) {
        this.type = type;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    /**
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }
    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}