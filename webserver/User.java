package webserver;

public class User {
    public int uid, type;
    public String username, name, ename, password;
    User(int uid_, String username_, String name_, String ename_, String password_, int type_) {
        this.uid = uid_;
        this.username = username_;
        this.name = name_;
        this.ename = ename_;
        this.password = password_;
        this.type = type_;
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
}