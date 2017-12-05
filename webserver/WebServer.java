package webserver;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class WebServer {
    /**
     * web服务器：实现200和404操作
     * 原理：
     * 服务器监听一个端口，并读取浏览器的请求信息，从该信息提取出访问的资源（这里为文件名）。并在工作目录下查找是否有该资源，有则输出资源内容，否则返回404
     * 测试方法：
     * 1、用String path=System.getProperty("user.dir");获取当前的工作目录，并在该目录下放要测试的文件
     * 2、访问127.0.0.1:8080/
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        JsonParser parse = new JsonParser();  //创建json解析器
        try {
            JsonObject json=(JsonObject) parse.parse(new FileReader("./webserver/JSONs/test.json"));  //创建jsonObject对象
            System.out.println("resultcode:"+json.get("resultcode").getAsInt());  //将json数据转为为int型的数据
            System.out.println("reason:"+json.get("reason").getAsString());     //将json数据转为为String型的数据
             
            JsonObject result=json.get("result").getAsJsonObject();
            JsonObject today=result.get("today").getAsJsonObject();
            System.out.println("temperature:" + today.get("temperature").getAsString());
             
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ServerSocket server = null;
        Socket s = null;
        try {
            server = new ServerSocket(8080, 3, InetAddress.getByName("127.0.0.1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                s = server.accept();
                InputStream input = s.getInputStream();

                //接收请求信息
                Request request = new Request(input);
                request.prework();
                String path = request.getPath();
                String params = request.getParams();
                String cookie = request.getCookie();
                
                System.out.println(path);
                System.out.println(params);
                System.out.println(cookie);
                //处理并相应请求信息
                Router router = new Router(path, params, cookie);
                
                //处理并响应请求信息

                OutputStream output = s.getOutputStream();
                Response response = new Response(output, path);
                response.response();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}