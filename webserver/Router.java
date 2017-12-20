package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

// 用于处理请求。
public class Router {
    private String path = null, cookie = null, type = null;
    private JsonObject params = null;
    private Gson gson = null;
    private Object content = null;
    //workers
    private UserAction ua = null;
    private Token ta = null;
    private MenuAction ma = null;
    private TaskAction taska = null;
    private GridAction ga = null;
    private FormAction fa = null;
    Router () {
        ua = new UserAction();
        ta = new Token();
        ma = new MenuAction();
        taska = new TaskAction();
        ga = new GridAction();
        fa = new FormAction();
        gson = new Gson();
    }
    public void setArgs(String path, JsonObject params, String cookie) {
        this.params = params;
        this.path = path;
        this.cookie = cookie;
    }

    public void route() {
        if (this.path.equals("/")) {
            this.type = "static";
            this.path = "/index.html";
        }
        this.type = "static";
        
        if (this.path.equals("/api/tasks")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            this.content = taska.getTasks(uid);
        }
    
        if (this.path.equals("/api/login")) {
            this.type = "POST";
            this.content = ua.verify(this.ta, this.params);
        }

        if (this.path.equals("/api/menu")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            this.content = ma.getMenu(type);
        }
        
        if (this.path.equals("/api/users/grid")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            if (type == 0) {
                this.content = ga.getGrid("users");
            }
        }

        if (this.path.equals("/api/users/form/modify")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            String toEdit = params.get("id").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            //if (type == 0) {
                this.content = fa.getForms(this.ua, "users:modify", toEdit);
            //}
        }


        if (this.path.equals("/api/users/form/create")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            if (type == 0) {
                this.content = fa.getForms(this.ua, "users:create", "233");
            }
        }
        if (this.path.equals("/api/users")) {
            this.type = "GET";
            /*String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            if (type == 0) {
                this.content = fa.getForms("users");
            }*/
            this.content = ua.getList(params);
        }
        if (this.path.equals("/api/users/create")) {
            this.type = "GET";
            String name = params.get("name").getAsString();
            int type = params.get("type").getAsInt();
            String username = params.get("username").getAsString();
            System.out.println(username);
            this.content = ua.addUser(username, type, name);
        }
    }  

    public String getType() {
        return this.type;
    }
    
    public String getPath() {
        return this.path;
    }

    public Object getContent() {
        return this.content;
    }

    /*
        Aim:提取一段文本处于一段文本中某两段文本之间的内容
        Params: str 原文本, a 处于前段的文本, b 处于后段的文本
        Result: str中a与b之间的内容 若找不到a或者b返回null
    */
    public String getBetween(String str, String a, String b) {
        int indexA = str.indexOf(a), indexB;
        if (indexA == -1)
            return null;
        indexB = str.indexOf(b, indexA + a.length());
        if (indexB == -1)
            return null;
        return str.substring(indexA + a.length(), indexB);
    }

    private static String input() {
        String res = null;
        try {  
            FileInputStream in = new FileInputStream("./webserver/Menus/menu_admin.js");
            byte bs[] = new byte[in.available()];  
            in.read(bs);
            res = new String(bs);
            in.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return res;
    }
}