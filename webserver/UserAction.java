package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.math.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import com.alibaba.fastjson.*;

// 用于处理请求。
public class UserAction {
    // Basic Things
    ArrayList<User> users = null;
    Gson gson = new Gson();
    UserAction() {
        Gson gs = new Gson();
        users = gs.fromJson(input("user.json"), new TypeToken<ArrayList<User>>(){}.getType());
    }
    
    //Particular Things
    public Message verify(Token token, JsonObject params) {
        Message res = new Message(403, "invalid", null, null);
        String username = params.get("username").getAsString();
        String password = params.get("password").getAsString();
        for (User p : users) {
            if (p.getUsername().equals(username)) {
                if (p.getPassword().equals(password))
                    res = new Message(200, null, new item(p), token.getToken(p.getUsername(), p.getUid()));
                break;
            }
        }
        return res;
    }
    public Message addUser(String username, int type, String name) {
        int uid = -1;
        Random rand = new Random();
        Boolean flag = true;
        while (flag) {
            uid = rand.nextInt(100000);
            System.out.println(uid);
            for (User p : users)
                if (p.getUid() == uid) {
                    flag = false;
                    break;
                }
            if (!flag) 
                break;
        }
        for (User p : users) {
            System.out.println(p.getUsername());
            if (p.getUsername().equals(username))
                return new Message(403, "repeated", null, null);
        }
        this.users.add(new User(uid, username, name, "123456", type));
        System.out.println(uid);
        output(gson.toJson(this.users), "user.json");
        return null;
    }

    public int getType(int uid) {
        for (User p : users) {
            if (p.getUid() == uid) 
                return p.getType();
        }
        return 3;
    }

    public Message queryUser(int uid) {
        Message res = new Message(403, "this user doesn't exist.", null, null);
        for (User p : users) {
            if (p.getUid() == uid) {
                res = new Message(200, null, new item(p), null);
                break;
            }
        }
        return res;
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
    public JSONArray getScore(int uid, int domainid) {
        System.out.println(uid);
        System.out.println(domainid);
        JSONObject usr = JSON.parseObject(input(uid + ".json"));
        System.out.println(usr);
        usr = usr.getJSONObject("scores");
        System.out.println(usr);
        JSONArray res = usr.getJSONArray(String.valueOf(domainid));
        System.out.println(res);
        return res;
    }

    
    //----General Things----
    private final static String filePath = "./webserver/JSONs/";
    private static String input(String fileName) {
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
    public static void output(String str, String fileName) {  
        try {  
            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(str.getBytes());  
            out.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
    }  
}


