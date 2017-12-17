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

// 用于处理请求。
public class FormAction {
    // Basic Things
    Gson gson = new Gson();
    JsonObject users;
    FormAction() {
        JsonParser parse = new JsonParser();
        this.users = (JsonObject) parse.parse(input("manage.json"));
    }

    //Particular Things
    public JsonObject getForms(String tag) {
        if (tag == "users") 
            return this.users;
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