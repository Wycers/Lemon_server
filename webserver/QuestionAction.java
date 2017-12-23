package webserver;

import java.io.*;
import java.util.HashMap;

import com.alibaba.fastjson.*;

public class QuestionAction {
    private int size = 9999;
    JSONArray questions = new JSONArray();
    public QuestionAction() {
        for (int i = 10000; i <= size; ++i)
            questions.add(JSON.parseObject(input(i + ".json")));    
    }
 
    public int createQuestion(String title, String body) {
        ++size;
        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("body", body);
        output(obj.toString(), size + ".json");
        return size;
    } 
    

    // ================
    private final static String filePath = "./webserver/Questions/";
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