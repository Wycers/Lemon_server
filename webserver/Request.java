package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.io.OutputStream;

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
        String strPage = null;
        String[] arrSplit = null;
        strURL = strURL.trim().toLowerCase();
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 0 && arrSplit.length > 1)
            if (arrSplit[0] != null)
                strPage = arrSplit[0];
        return strPage;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1 && arrSplit.length > 1)
            if (arrSplit[1] != null)
                strAllParam = arrSplit[1];
        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * @param URL  url地址
     * @return  url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null)
            return mapRequest;

        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            if (arrSplitEqual.length > 1)
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            else if (arrSplitEqual[0] != "")
                mapRequest.put(arrSplitEqual[0], "");
        }
        return mapRequest;
    }

    InputStream input;
    OutputStream output;
    private String content = null, action = null;

    public Request(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void prework() throws IOException {
        StringBuffer request = new StringBuffer();
        byte[] buffer = new byte[128 * 1024];
        int i = 0;
        do {
            i = 0;
            try {
                i = input.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                i = -1;
            }
            if (0 < i)
                request.append(new String(buffer, 0, i, "ISO-8859-1"));
        } while (i >= 128 * 1024);
        this.content = request.toString();

        this.content = this.content.replaceAll("%22", "'");
        this.content = this.content.replaceAll("%7B", "{");
        this.content = this.content.replaceAll("%7D", "}");
        this.action = getBetween(this.content, "", " ");

        // System.out.println(this.content);
        /*System.out.println(this.content);
        if (this.action.equals("POST")) {
            String str = "HTTP/2.0 200 OK \r\n" + "\r\n";
            output.write(str.getBytes());
            i = 0;
            try {
                i = input.read(buffer); 
            } catch (IOException e) {
                e.printStackTrace();
                i = -1;
            }
            for (int k = 0; k < i; k++) 
                request.append((char) buffer[k]);
            this.content = request.toString();
        }*/
    }

    public void gg(String path, byte[] content) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(content);
        fos.close();
    }

    MD5 md5 = new MD5();

    public String createFile() throws IOException {
        String splitTag = "--" + getBetween(this.content, "boundary=", "\r\n");
        String[] parts = this.content.split(splitTag);
        String res = parts[2];

        String fileName = getBetween(res, "filename=\"", "\"");
        String type = getBetween(fileName + "|", ".", "|");
        fileName = md5.getMD5(getBetween("|" + fileName, "|", ".") + System.currentTimeMillis()) + "." + type;
        res = res.substring(res.indexOf("\r\n\r\n") + 4);
        try {
            File newFile = new File("./static/" + fileName);
            if (newFile.exists())// 存在，则删除  
                if (!newFile.delete())// 删除成功则创建      
                    System.err.println("删除文件" + newFile + "失败");
            if (newFile.createNewFile())
                gg("./static/" + fileName, res.getBytes("ISO-8859-1"));
            else {
                System.err.println("创建文件：" + newFile + "失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public JsonObject getParams() throws IOException {
        if (this.content == null)
            return null;
        if (this.getPath().equals("/api/upload")) {
            createFile();
            return null;
        }
        String res = null;
        Gson gson = new Gson();
        if (this.action.equals("GET")) {
            String str = getBetween(this.content, " ", " ");

            String strRequestKeyAndValues = "";
            Map<String, String> mapRequest = URLRequest(str);
            res = gson.toJson(mapRequest);
        } else {
            String str = getBetween(this.content + "\n", "{", "}\n");
            if (str == null)
                return null;
            res = "{" + str + "}";
        }
        JsonParser parse = new JsonParser();
        return (JsonObject) parse.parse(res);
    }

    public String getPath() {
        if (this.content == null)
            return null;
        if (this.content.startsWith("OPTIONS"))
            return "OPTIONS";
        if (this.content.startsWith("POST"))
            return getBetween(this.content, " ", " ");
        if (this.content.startsWith("GET")) {
            String res = getBetween(this.content, " ", " ");
            if (res.indexOf("?") >= 0)
                return getBetween(res, "", "?");
            return res;
        }
        return null;
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