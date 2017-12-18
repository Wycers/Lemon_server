package webserver;
public class item {
    private int uid, type;
    private String username, name;
    item(int uid, int type, String username, String name) {
        this.uid = uid;
        this.type = type;
        this.username = username;
        this.name = name;
    }
    public int getUid() {
        return this.uid;
    }
    public int getType() {
        return this.type;
    }
    public String getUsername() {
        return this.username;
    }
    public String getName() {
        return this.name;
    }
}