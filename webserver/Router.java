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
    private String params, path, cookie;
    public Router(String path, String params, String cookie) {
        this.params = params;
        this.path = path;
        this.cookie = cookie;
    }
    
    public void route() {
        String temp;
        if (this.path.equals("/"))
            path = "/index.html";
        while (this.path.length() > 0) {
            temp = getBetween(this.path, "/", "/");
            this.path = this.path.substring(this.path.indexOf("/", 1));
        }
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
}