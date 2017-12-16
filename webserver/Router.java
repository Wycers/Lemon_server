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
    Router () {
        ua = new UserAction();
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
            task[] tasks = new task[1];
            this.type = "GET";
            tasks[0] = new task();
            this.content = tasks;
        }
        
    
        if (this.path.equals("/api/login")) {
            this.type = "POST";
            this.content = ua.verify(this.params);
            System.out.println("233");
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
    
    public class task {
        public int id;
        public String item, eitem, content, ddl, color;
        public Boolean finished;
        public task() {
            id = 2;
            item = "线代作业";
            eitem = "Linear Algebra";
            content = "Section 3.5 : 1 3 5 7 9";
            ddl = "2017-11-3";
            color = "#6f6f6f";
            finished = true;
        }
    }
}