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
import com.google.gson.reflect.TypeToken;

// 用于处理请求。
public class TaskAction {
    // Basic Things
    private final static String fileName = "task.json";
    ArrayList<Tasks> tasks = null;
    Gson gson = new Gson();
    public class Task {
        public int id;
        public String item, eitem, content, ddl, color;
        public Boolean finished;
        public Task() {
            id = 2;
            item = "线代作业";
            eitem = "Linear Algebra";
            content = "Section 3.5 : 1 3 5 7 9";
            ddl = "2017-11-3";
            color = "#6f6f6f";
            finished = true;
        }
    }
    public class Tasks {
        public int uid;
        private ArrayList<Task> tasks = null;
        public int getUid() {
            return this.uid;
        }
        public ArrayList<Task> getTasks() {
            return this.tasks;
        }
    }
    TaskAction() {
        Gson gs = new Gson();
        tasks = gs.fromJson(input(), new TypeToken<ArrayList<Tasks>>(){}.getType());
        for (Tasks t : tasks)
            System.out.println(t.uid);
    }

    //Particular Things
    public ArrayList<Task> getTasks(int uid) {
        for (Tasks task : tasks) {
            if (task.getUid() == uid) {
                return task.getTasks();
            }
        }
        return null;
    }
    
    //----General Things----
    private final static String filePath = "./webserver/JSONs/";
    private static String input() {
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
    public static void output(String str) {  
        try {  
            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(str.getBytes());  
            out.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
    }  
}