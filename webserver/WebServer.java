package webserver;

import java.io.*;
import java.net.*;
import java.lang.*;

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
        ServerSocket server = null;
        Router router = new Router(); 
        Gson gson = new Gson();
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
                OutputStream output = s.getOutputStream();

                //接收请求信息
                Request request = new Request(input, output);
                request.prework();
                String path = request.getPath();
                JsonObject params = request.getParams();
                String cookie = request.getCookie();
                
                //处理请求信息
                router.setArgs(path, params, cookie); 
                router.route();
                String type = router.getType();
                String newPath = router.getPath();
                Object content = router.getContent();

                //响应请求信息
                Response response = new Response(output, type, newPath, gson.toJson(content));
                response.response();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}