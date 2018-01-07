package webserver;

public class User {
    private int uid, type;
    private String username, name, password, avatar;
    public User() {}
    public User(int uid, String username, String name, String password, int type) {
        this.uid = uid;
        this.username = username;
        this.name = name;
        this.password = password;
        this.type = type;
        this.avatar = "/static/1.jpg";
    }
    public User(int uid, int type, String username, String name, String password, String avatar) {
        this.uid = uid;
        this.type = type;
        this.username = username;
        this.name = name;
        this.password = password;
        this.avatar = avatar;
    }

    /**
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
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
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }
    /**
     * @param uid the uid to set
     */
    public void setUid(int uid) {
        this.uid = uid;
    }
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
}