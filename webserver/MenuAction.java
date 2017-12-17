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
public class MenuAction {
    // Basic Things
    Gson gson = new Gson();
    JsonArray teacher, student, admin;
    MenuAction() {
        JsonParser parse = new JsonParser();
        this.admin = (JsonArray) parse.parse(input("menu_admin.js"));
        this.teacher = (JsonArray) parse.parse(input("menu_teacher.js"));
        this.student = (JsonArray) parse.parse(input("menu_student.js"));
    }

    //Particular Things
    public JsonArray getMenu(int type) {
        if (type == 0) 
            return this.admin;
        if (type == 1)
            return this.teacher;
        if (type == 2)
            return this.student;
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