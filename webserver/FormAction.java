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
    Gson gson = new Gson();
    JSONObject users;

    FormAction() {
        //JsonParser parse = new JsonParser();
        //this.users = (JsonObject) parse.parse(input("manage.json"));
        JSONObject users = JSON.parseObject(input("manage.json"));
        /*
        System.out.println("======before=====");
        System.out.println("size: " + json.size());
        System.out.println("val:  " + json.get("model"));
        json.put("model", 234); // 直接put相同的key
        System.out.println("======after======");
        System.out.println("size: " + json.size());
        System.out.println("val:  " + json.get("model"));

        //Gson gs = new Gson();
        //System.out.println(gs.toJson(qwq));*/
    }

    //Particular Things
    public Object getForms(UserAction ua, String tag, String toEdit) {
        if (tag.equals("users")) {
            Gson gs = new Gson();
            item temp = ua.getUser(Integer.parseInt(toEdit));
            System.out.println(JSON.toJSON(temp));
            
            JSONObject json = JSON.parseObject(input("manage.json"));
            System.out.println("size: " + json.size());
            System.out.println("val:  " + json.get("model"));
            json.put("model", JSON.toJSON(temp)); // 直接put相同的key
            System.out.println("======after======");
            System.out.println("size: " + json.size());
            System.out.println("val:  " + json.get("model"));
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