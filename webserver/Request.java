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

public class Request {
    /*
     * 接收请求的信息，并返回资源（文件名）
     * */

    public static String UrlPage(String strURL) {
        String strPage=null;
        String[] arrSplit=null;
        strURL = strURL.trim().toLowerCase();
        arrSplit = strURL.split("[?]");
        if(strURL.length() > 0 && arrSplit.length > 1)
            if(arrSplit[0] != null)
                strPage = arrSplit[0];
        return strPage;
    }
    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL)
    {
        String strAllParam = null;
        String[] arrSplit = null;
     
        strURL=strURL.trim().toLowerCase();
        arrSplit=strURL.split("[?]");
        if(strURL.length() > 1 && arrSplit.length > 1)
            if(arrSplit[1] != null)                  
                strAllParam=arrSplit[1];
        return strAllParam;   
    }
    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * @param URL  url地址
     * @return  url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL)
    {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit=null;
        String strUrlParam=TruncateUrlPage(URL);
        if(strUrlParam == null)
            return mapRequest;
            
        arrSplit = strUrlParam.split("[&]");
        for(String strSplit:arrSplit)
        {
            String[] arrSplitEqual=null;         
            arrSplitEqual = strSplit.split("[=]");

            if(arrSplitEqual.length > 1)
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            else if(arrSplitEqual[0]!="")
                mapRequest.put(arrSplitEqual[0], "");
        }   
        return mapRequest;   
    }
    InputStream input;
    private String content = null;
    public Request(InputStream input) {
        this.input = input;
    }

    public void prework() throws IOException {
        StringBuffer request = new StringBuffer();
        byte[] buffer = new byte[2048];
        int i = 0;

        try {
            i = input.read(buffer); 
            //读取内容并存入buffer数组中，并返回读取的的字节数。
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }

        //将buffer数组转换为字符串
        for (int k = 0; k < i; k++) 
            request.append((char) buffer[k]);
        this.content = request.toString();
    }

    /*提取文件名*/
    
    //查找
    public String getParams() {
        if (this.content == null)
            return null;
        String str = getBetween(this.content, " ", " ");    
        
         //url参数键值对
        String strRequestKeyAndValues = "";       
        Map<String, String> mapRequest = URLRequest(str);

        Gson gson = new Gson();
        String json = gson.toJson(mapRequest);
        return json;
    }
    public String getPath() {
        if (this.content == null)
            return null;
        if (getBetween(this.content, " ", "?") == null)
            return getBetween(this.content, " ", " ");
        return getBetween(this.content, " ", "?");
    }
    public String getCookie() {
        if (this.content == null)
            return null;
        return getBetween(this.content, "Cookie: ", "\n");
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