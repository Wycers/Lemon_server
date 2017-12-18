package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.math.*;

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
        users = gs.fromJson(input(), new TypeToken<ArrayList<User>>(){}.getType());
    }

    //Particular Things
    public static class Message {
        private int status;
        private String message, token;
        private User user = null;
        Message(int sta_, String msg_, User user_, String token) {
            this.status = sta_;
            this.message = msg_;
            this.user = user_;
            this.token = token;
        }
    }
    public Message verify(Token token, JsonObject params) {
        Message res = new Message(403, "invalid", null, null);
        String username = params.get("username").getAsString();
        String password = params.get("password").getAsString();
        for (User p : users) {
            if (p.getUsername().equals(username)) {
                if (p.getPassword().equals(password))
                    res = new Message(200, null, p, token.getToken(p.getUsername(), p.getUid()));
                break;
            }
        }
        return res;
    }
    public int getType(int uid) {
        for (User p : users) {
            if (p.getUid() == uid) 
                return p.getType();
        }
        return 3;
    }

    public class Result {
        int currentPage, lastPage, perPage, total;
        Object data;
        Result(int cPage, int lPage, int pPage, int total, Object data) {
            this.currentPage = cPage;
            this.lastPage = lPage;
            this.perPage = pPage;
            this.total = total;
            this.data = data;
        }
    }
    private static int min(int a, int b) {
        return a < b ? a : b;
    }
    public item getUser(int uid) {
        for (User p : users) {
            if (p.getUid() == uid)
                return new item(p.getUid(), p.getType(), p.getUsername(), p.getName());
        }
        return null;
    }
    public Object getList(JsonObject params) {
        System.out.println(params);
        
        int page = params.get("page").getAsInt();
        int perPage = params.get("perpage").getAsInt();
        String querys = params.get("query").getAsString();
        querys = querys.substring(0, querys.length());
        System.out.println(querys);
        String username = "", type = "";
        if (!querys.equals("{}")) {
            JsonParser parse = new JsonParser();
            JsonObject query = (JsonObject) parse.parse(querys);
            username = query.get("username").getAsString();
            type = query.get("type").getAsString();
        }
        
        ArrayList<item> list = new ArrayList<item>();
        for (User p : this.users) {
            if (!username.isEmpty() && !p.getUsername().equals(username))
                continue;
            if (!type.isEmpty()) 
                if (!type.equals("3") && Integer.parseInt(type) != p.getType())
                    continue;
            list.add(new item(p.getUid(), p.getType(), p.getUsername(), p.getName()));
        }
        int offset = (page - 1) * perPage;
        ArrayList<item> res = new ArrayList<item>();
        for (int i = offset; i < min(offset + perPage, list.size()); ++i)
            res.add(list.get(i));
        return new Result(page, (int)Math.ceil(list.size() / perPage), perPage, list.size(), res);
    }
    
    
    //----General Things----
    private final static String filePath = "./webserver/JSONs/";
    private static String input() {
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


