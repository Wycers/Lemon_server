package webserver;

import java.io.*;
import java.nio.channels.NetworkChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.alibaba.fastjson.*;

// 用于处理请求。
public class MenuAction {
    // Basic Things
    Gson gson = new Gson();
    JSONArray teacher, student, admin, appointment;
    MenuAction() {
        this.student = JSON.parseArray(input("menu_student.json"));
        this.teacher = JSON.parseArray(input("menu_teacher.json"));
        this.admin = JSON.parseArray(input("menu_admin.json"));

        this.appointment = JSON.parseArray(input("menu_appointment.json"));
    }

    //Particular Things
    public JSONArray getMenu(String type) {
        if (type == "appointment")
            return this.appointment;
        return null;
    }

    public JSONArray getMenu(JSONArray domain, int type) {
        if (type == 4) 
            return this.appointment;
        if (type == 0) 
            return this.admin;
        if (type == 1) {
            JSONArray json = this.teacher;
            for (int i = 0, len = json.size(); i < len; i++) {
                JSONObject temp = json.getJSONObject(i);
                String title = temp.getString("title");
                if (title == null) 
                    continue; 
                if (title.equals("Domain")) {
                    temp.put("items", domain); //将“items”字段对应的内容改为domain
                    json.set(i, temp);
                }
            }
            return json;
        }
        if (type == 2) {
            JSONArray json = this.student;
            for (int i = 0, len = json.size(); i < len; i++) {
                JSONObject temp = json.getJSONObject(i);
                String title = temp.getString("title");
                if (title == null) 
                    continue; 
                if (title.equals("Domain")) {
                    temp.put("items", domain);
                    json.set(i, temp);
                }
            }
            return json;
        }
        return null;  
    }
    
    //----General Things----
    private final static String filePath = "./webserver/Menus/";
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