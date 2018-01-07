package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.math.*;

import com.alibaba.fastjson.*;

// 用于处理请求。
public class DomainAction {
    // Basic Things

    HashMap<Integer, JSONObject> did2domain = new HashMap<Integer, JSONObject>();
    HashMap<Integer, JSONArray> uid2domain = new HashMap<Integer, JSONArray>();
    JSONArray domains = null;

    private void Add(JSONArray T, JSONObject content) {
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
    
    private void delete(JSONArray T, int did) {
        for (int i = 0, len = T.size(); i < len; ++i) {
            JSONObject usr = T.getJSONObject(i);
            int uid = usr.getInteger("uid");
            JSONArray temp = uid2domain.get(uid);

            if (temp == null)
                return;
            for (int j = 0, len2 = temp.size(); j < len2; ++j) 
                if (temp.getJSONObject(j).getInteger("id") == did) {
                    temp.remove(j);
                    break;
                }
        }
    }

    public static String recover(String str) throws Throwable {
        return new String(str.getBytes("GBK"), "UTF-8");
    }

    DomainAction() {
        domains = JSON.parseArray(input("domain.json"));
        for (int i = 0, len = domains.size(); i < len; ++i) {
            JSONObject now = domains.getJSONObject(i);
            did2domain.put(now.getInteger("id"), now);

            DomainDetail tmp = JSON.parseObject(input("domain_" + now.getInteger("id") + ".json"), DomainDetail.class);
            Add(tmp.getLecturer(), now);
            Add(tmp.getStudents(), now);
            Add(tmp.getAssistant(), now);
            Add(tmp.getAdministrator(), now);
        }
    }

    //Particular Things
    public Boolean check() {
        return true;
    }

    public void pick(JSONArray res, JSONArray T) {
        for (int i = 0, len = T.size(); i < len; ++i) 
            res.add(T.getJSONObject(i));
    }
    public JSONObject getDomain(int did) {
        JSONArray tmp2 = JSON.parseArray(input("domain.json"));
        JSONObject res = new JSONObject();

        for (int i = 0, len = tmp2.size(); i < len; ++i)  {
            JSONObject temp = tmp2.getJSONObject(i);
            if (temp.getInteger("id") == did) {
                res.put("domaintitle", temp.getString("title"));
                res.put("domainetitle", temp.getString("etitle"));
                break;
            }
        }

        JSONObject tmp = JSON.parseObject(input("domain_" + did + ".json"));
        JSONArray users = new JSONArray();
        pick(users, tmp.getJSONArray("administrator"));
        pick(users, tmp.getJSONArray("lecturer"));
        pick(users, tmp.getJSONArray("assistant"));
        pick(users, tmp.getJSONArray("students"));
        res.put("users", users);
        return res;
    }

    public JSONObject newDomain(int domainid, String title, String etitle) {
        JSONObject res = new JSONObject();
        res.put("id", domainid);
        res.put("href", "/domain/" + domainid);
        res.put("title", title);
        res.put("etitle", etitle);
        return res;
    }

    public Message addDomain(String title, String etitle, JSONArray users) {
        int did = -1;
        Random rand = new Random();
        Boolean flag = true;
        while (flag) {
            did = rand.nextInt(100000);
            if (did2domain.get(did) == null)
                break;
        }
        JSONObject temp = newDomain(did, title, etitle);
        did2domain.put(did, temp);

        domains.add(temp);
        output(JSON.toJSON(domains).toString(), "domain.json");

        JSONObject obj = new JSONObject();
        JSONArray administrator = new JSONArray();
        JSONArray lecturer = new JSONArray();
        JSONArray assistant = new JSONArray();
        JSONArray students = new JSONArray();

        obj.put("domainid", did);
        for (int i = 0, len = users.size(); i < len; i++) {
            JSONObject usr = users.getJSONObject(i);
            int uid = usr.getInteger("uid");
            if (usr.getString("type").equals("administrator"))
                administrator.add(usr);
            if (usr.getString("type").equals("lecturer"))
                lecturer.add(usr);
            if (usr.getString("type").equals("assistant"))
                assistant.add(usr);
            if (usr.getString("type").equals("student"))
                students.add(usr);
        }
        Add(administrator, temp);
        Add(lecturer, temp);
        Add(students, temp);
        Add(assistant, temp);
        obj.put("administrator", administrator);
        obj.put("lecturer", lecturer);
        obj.put("assistant", assistant);
        obj.put("students", students);

        output(JSON.toJSON(obj).toString(), "domain_" + did + ".json");
        return new Message(200, null, null, null);
    }

    public Message editDomain(int did, String title, String etitle, JSONArray users) {
        JSONObject temp = newDomain(did, title, etitle);
        did2domain.put(did, temp);
        for (int i = 0, len = domains.size(); i < len; ++i)
            if (domains.getJSONObject(i).getInteger("id") == did) {
                domains.set(i, temp);
                break;
            }
        output(JSON.toJSON(domains).toString(), "domain.json");

        JSONObject obj = new JSONObject();
        JSONArray administrator = new JSONArray();
        JSONArray lecturer = new JSONArray();
        JSONArray assistant = new JSONArray();
        JSONArray students = new JSONArray();

        obj.put("domainid", did);
        for (int i = 0, len = users.size(); i < len; i++) {
            JSONObject usr = users.getJSONObject(i);
            int uid = usr.getInteger("uid");
            if (usr.getString("type").equals("administrator"))
                administrator.add(usr);
            if (usr.getString("type").equals("lecturer"))
                lecturer.add(usr);
            if (usr.getString("type").equals("assistant"))
                assistant.add(usr);
            if (usr.getString("type").equals("student"))
                students.add(usr);
        }
        delete(getDomain(did).getJSONArray("users"), did);
        
        Add(administrator, temp);
        Add(lecturer, temp);
        Add(students, temp);
        Add(assistant, temp);
        obj.put("administrator", administrator);
        obj.put("lecturer", lecturer);
        obj.put("assistant", assistant);
        obj.put("students", students);

        output(JSON.toJSON(obj).toString(), "domain_" + did + ".json");
        return new Message(200, null, null, null);
    }

    public JSONObject getDomainDetail(int did) {
        JSONObject domain = did2domain.get(did);
        if (domain == null) {
            return null;
        }
        return JSON.parseObject(input("domain_" + did + ".json"));
    }

    public JSONArray getDomains(int uid) {
        JSONArray res = new JSONArray();
        JSONArray tmp = uid2domain.get(uid);
        if (tmp != null)
            res = (JSONArray)tmp.clone();
        res.add(JSON.parseObject("{\"href\": \"/domain/create\", \"title\": \"创建域\", \"etitle\": \"create domain\"}"));
        return res;
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

    public static void output(String str, String fileName) {
        File file = new File(filePath + fileName);
        try {
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(str.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
