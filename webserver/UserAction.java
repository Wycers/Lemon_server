package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

// 用于处理请求。
public class UserAction {
    // Basic Things
    private final static String fileName = "user.json";
    ArrayList<User> users = null;
    Gson gson = new Gson();
    UserAction() {
        Gson gs = new Gson();
        users = gs.fromJson(input(), new TypeToken<ArrayList<User>>(){}.getType());//把JSON格式的字符串转为List  
        for (User p : users) {  
            System.out.println("把JSON格式的字符串转为List///  "+p.toString());  
        }
    }
    public static class User {
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
    }

    //Particular Things
    public static class Message {
        private int status;
        private String message;
        private User user = null;
        Message(int sta_, String msg_, User user_) {
            this.status = sta_;
            this.message = msg_;
            this.user = user_;
        }
    }
    public Message verify(JsonObject params) {
        Message res = new Message(403, "invalid", null);
        String username = params.get("username").getAsString();
        String password = params.get("password").getAsString();
        System.out.println(username);
        System.out.println(password);
        for (User p : users) {
            if (p.getUsername().equals(username)) {
                if (p.getPassword().equals(password))
                    res = new Message(200, null, p);
                break;
            }
        }
        return res;
    }
    
    
    //----General Things----
    private final static String filePath = "./webserver/JSONs/";
    private static String input(){
        String res = null;
        try {  
            FileInputStream in = new FileInputStream(filePath + fileName);
            byte bs[] = new byte[in.available()];  
            in.read(bs);
            res = new String(bs);
            in.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return res;
    }
    public static void output(String str) {  
        try {  
            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(str.getBytes());  
            out.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
    }  
}