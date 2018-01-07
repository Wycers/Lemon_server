package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.math.*;

import com.alibaba.fastjson.*;

// 用于处理请求。
public class UserAction {
    // Basic Things
    List<User> users = null;
    UserAction() {
        users = JSON.parseArray(input("user.json"), User.class);
    }
    
    //Particular Things
    public Message verify(Token token, JSONObject params) {
        Message res = new Message(403, "invalid", null, null);
        String username = params.getString("username");
        String password = params.getString("password");
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
        for (User p : users) {
            if (p.getUsername().equals(username))
                return new Message(403, "repeated", null, null);
        }
        int uid = -1;
        Random rand = new Random();
        Boolean flag = true;
        while (flag) {
            uid = rand.nextInt(100000);
            flag = false;
            for (User p : users)
                if (p.getUid() == uid) {
                    flag = true;
                    break;
                }
        }
        this.users.add(new User(uid, username, name, "123456", type));
        output(JSON.toJSONString(this.users), "user.json");
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
        for (User p : users) 
            if (p.getUid() == uid) {
                res = new Message(200, null, new item(p), null);
                break;
            }
        return res;
    }

    
    private static int min(int a, int b) {
        return a < b ? a : b;
    }
    public item getUser(int uid) {
        for (User p : users) {
            if (p.getUid() == uid)
                return new item(p);
        }
        return null;
    }
    public Object getList(JSONObject params) {
        int page = params.getInteger("page");
        int perPage = params.getInteger("perpage");
        String querys = params.getString("query");
        querys = querys.substring(0, querys.length());
        String username = "", type = "";
        if (!querys.equals("{}")) {
            JSONObject query = JSON.parseObject(querys);
            username = query.getString("username");
            type = query.getString("type");
        }
        
        ArrayList<item> list = new ArrayList<item>();
        for (User p : this.users) {
            if (!username.isEmpty() && !p.getUsername().equals(username))
                continue;
            if (!type.isEmpty()) 
                if (!type.equals("3") && Integer.parseInt(type) != p.getType())
                    continue;
            list.add(new item(p));
        }
        int offset = (page - 1) * perPage;
        ArrayList<item> res = new ArrayList<item>();
        for (int i = offset; i < min(offset + perPage, list.size()); ++i)
            res.add(list.get(i));
        return new Result(page, (int)Math.ceil(list.size() / perPage), perPage, list.size(), res);
    }
    
    public JSONArray getScore(int uid, int domainid) {
        JSONObject usr = JSON.parseObject(input(uid + ".json"));
        usr = usr.getJSONObject("scores");
        JSONArray res = usr.getJSONArray(String.valueOf(domainid));
        return res;
    }

    public JSONObject getUserInfo(int uid) {
        JSONObject res = new JSONObject(); 
        for (User p : users) 
            if (p.getUid() == uid) {
                res.put("uid", uid);
                res.put("avatar", p.getAvatar());
                res.put("name", p.getName());
                return res;
            }
        return null;
    }

    
    //----General Things----
    private final static String filePath = "./webserver/Users/";
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


