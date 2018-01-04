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

import com.alibaba.fastjson.*;

// 用于处理请求。
public class FormAction {
    // Basic Things
    
    //Particular Things
    public JSONObject getForms(String tag) {
        if (tag.equals("users:create")) {
            JSONObject json = JSON.parseObject(input("create.json"));
            return json;
        } 
        if (tag.equals("appointment")) {
            JSONObject json = JSON.parseObject(input("appointment.json"));
            return json;
        }
        System.out.println("Something goes wrong. Please check usage of <getForms>");
        return null;
    }
    public JSONObject getForms(UserAction ua, String tag, int toEdit) {
        if (tag.equals("users:modify")) {
            item temp = ua.getUser(toEdit);
            JSONObject json = JSON.parseObject(input("manage.json"));
            json.put("model", JSON.toJSON(temp)); 
            return json;
        } 
        if (tag.equals("settings")) {
            item temp = ua.getUser(toEdit);
            JSONObject json = JSON.parseObject(input("settings.json"));
            json.put("model", JSON.toJSON(temp)); 
            return json;
        }
        return null;  
    }
    
    //----General Things----
    private final static String filePath = "./webserver/Forms/";
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