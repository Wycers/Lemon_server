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


    public static String recover(String str) throws Throwable {
        return new String(str.getBytes("GBK"), "UTF-8");
    }

    DomainAction() {
        System.out.print("b");
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

    public JSONObject getDomain(int did) {
        return did2domain.get(did);
    }

    public Domain newDomain(int domainid, String title, String etitle) {
        Domain res = new Domain();
        res.setDomainId(domainid);
        JSONObject obj = new JSONObject();
        obj.put("href", "/domain/" + domainid);
        obj.put("title", title);
        obj.put("etitle", etitle);
        res.setContent(obj);
        return res;
    }

    public Message addDomain(UserAction ua, String title, String etitle, JSONArray users) {
        int did = -1;
        Random rand = new Random();
        Boolean flag = true;
        while (flag) {
            did = rand.nextInt(100000);
            if (this.getDomain(did) == null)
                break;
        }
        Domain temp = newDomain(did, title, etitle);
        JSONObject content = JSON.parseObject(JSON.toJSON(temp).toString());
        content = content.getJSONObject("content");
        System.out.println(content);
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
            usr.put("name", ua.getUser(uid).getName());
            if (usr.getString("type").equals("administrator"))
                administrator.add(usr);
            if (usr.getString("type").equals("lecturer"))
                lecturer.add(usr);
            if (usr.getString("type").equals("assistant"))
                assistant.add(usr);
            if (usr.getString("type").equals("student"))
                students.add(usr);
            System.out.println(usr);
        }
        Work(administrator, content);
        Work(lecturer, content);
        Work(students, content);
        Work(assistant, content);
        obj.put("administrator", administrator);
        obj.put("lecturer", lecturer);
        obj.put("assistant", assistant);
        obj.put("students", students);

        output(JSON.toJSON(obj).toString(), "domain_" + did + ".json");
        return new Message(200, null, null, null);
    }

    public void editDomain() {

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
        res.add(JSON.parseObject("{\"href\": \"/domain/create\", \"title\": \"create domain\"}"));
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
