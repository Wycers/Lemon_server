package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.math.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
 
import com.alibaba.fastjson.*;

// 用于处理请求。
public class DomainAction {
    // Basic Things

    HashMap<Integer, JSONObject> did2domain = new HashMap<Integer, JSONObject>();
    HashMap<Integer, JSONArray> uid2domain = new HashMap<Integer, JSONArray>();
    List<Domain> domains = null;
    private void Work(JSONArray T, JSONObject content) {
        for (int i = 0, len = T.size(); i < len; i++) {
            JSONObject usr = T.getJSONObject(i);
            int uid = usr.getInteger("uid");
            JSONArray temp = uid2domain.get(uid);
            if (temp == null) 
                temp = new JSONArray();
            temp.add(content);
            uid2domain.put(uid, temp);
        }
    }
    DomainAction() {
        //Gson gs = new Gson();
        //this.domains = gs.fromJson(input("domain.json"), new TypeToken<ArrayList<Domain>>(){}.getType());
        domains = JSON.parseArray(input("domain.json"), Domain.class);
        for (Domain domain : this.domains) 
            did2domain.put(domain.getDomainId(), domain.getContent());
        
        for (Domain domain : this.domains) {
            int domainid = domain.getDomainId();
            JSONObject content = domain.getContent();
            DomainDetail tmp = JSON.parseObject(input("domain_" + domainid + ".json"), DomainDetail.class);
            
            Work(tmp.getLecturer(), content);
            Work(tmp.getStudents(), content);
            Work(tmp.getAssistant(), content);
            Work(tmp.getAdministrator(), content);
        }
    }
    
    
    //Particular Things
    public Boolean check() {
        
        return true;
    }
    public void addDomain() {
        int did = -1;
        Random rand = new Random();
        Boolean flag = true;
        while (flag) {
            did = rand.nextInt(100000);
            System.out.println(did);
            if (!flag) 
                break;
        }
    }
    public void editDomain() {
        
    }
    public void getDomainDetail() {

    }
    public JSONArray getDomains(int uid) {
        if (uid2domain.get(uid) == null) {
            return new JSONArray();
        }
        return uid2domain.get(uid);
    }

    
    
    //----General Things----
    private final static String filePath = "./webserver/Domains/";
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
    public static void output(String str,String fileName) {  
        try {  
            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(str.getBytes());  
            out.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
    }  
}


