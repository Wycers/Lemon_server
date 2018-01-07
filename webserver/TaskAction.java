package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import com.alibaba.fastjson.*;

// 用于处理请求。
public class TaskAction {
    // Basic Things
    ArrayList<Task> tasks = null;
    TaskAction() {
        tasks = new ArrayList<Task>();
    }

    //Particular Things
    public Message addTask(int domainid, Task task) {
        JSONArray now = JSON.parseArray(input(domainid + ".json"));
        now.add(task);
        output(now.toString(), domainid + ".json");
        return new Message(200, null, null, null);
    }
    public JSONArray getTask(int domainid) {
        return JSON.parseArray(input(domainid + ".json"));
    }
    public JSONArray getTasks(DomainAction da, int uid) {
        JSONArray tmp = da.getDomains(uid);
        JSONArray res = new JSONArray();
        if (tmp == null)
            return res;
            for (int i = 0, len = tmp.size() - 1; i < len; ++ i) {
                JSONObject now = tmp.getJSONObject(i);
                JSONArray temp = getTask(now.getInteger("id"));
                for (int j = 0, len2 = temp.size(); j < len2; ++j) 
                    res.add(temp.getJSONObject(j));
            }
        return res;
    }
    
    //----General Things----
    private final static String filePath = "./webserver/Tasks/";
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