package webserver;

import java.io.*;
import java.util.HashMap;

import com.alibaba.fastjson.*;

public class AppointmentAction {
    HashMap<Integer, Integer> bidtotid = new HashMap<Integer, Integer>();
    HashMap<Integer, JSONArray> uidtoapp = new HashMap<>();
    JSONArray apps = null;
    JSONArray status = new JSONArray();

    public AppointmentAction() {
    
        JSONObject sta = new JSONObject();
        sta.put("status", 0);
        sta.put("editable", false);
        sta.put("deletable", false);
        sta.put("cancelable", true);
        status.add(sta);

        sta = new JSONObject();
        sta.put("status", 1);
        sta.put("editable", true);
        sta.put("deletable", false);
        sta.put("cancelable", true);
        status.add(sta);

        sta = new JSONObject();
        sta.put("status", 2);
        sta.put("editable", false);
        sta.put("deletable", true);
        sta.put("cancelable", false);
        status.add(sta);

        sta = new JSONObject();
        sta.put("status", 3);
        sta.put("editable", false);
        sta.put("deletable", true);
        sta.put("cancelable", false);
        status.add(sta);
        
        apps = JSON.parseArray(input("appointments.json"));
        for (int i = 0, len = apps.size(); i < len; ++i) {
            JSONObject p = apps.getJSONObject(i);

            JSONArray tmp = new JSONArray();
            tmp = uidtoapp.get(p.getInteger("suid"));
            if (tmp == null)
                tmp = new JSONArray();
            tmp.add(p);
            uidtoapp.put(p.getInteger("suid"), tmp);

            tmp = uidtoapp.get(p.getInteger("tuid"));
            if (tmp == null)
                tmp = new JSONArray();
            tmp.add(p);
            uidtoapp.put(p.getInteger("tuid"), tmp);
        }
    }

    public JSONArray getAppointment(UserAction ua, BlockAction ba, int uid) {
        JSONArray origin = uidtoapp.get(uid);
        if (origin == null)
            return null;
        JSONArray res = new JSONArray();
        for (int i = 0, len = res.size(); i < len; ++i) {
            JSONObject now = res.getJSONObject(i);
            JSONObject tmp = status.getJSONObject(now.getInteger("status"));
            tmp.put("s", ua.getUserInfo(now.getInteger("suid")));
            tmp.put("t", ua.getUserInfo(now.getInteger("tuid")));
            tmp.put("b", ba.getBlockInfo(now.getInteger("bid")));
            tmp.put("aid", now.getInteger("aid"));
            res.add(tmp);
        }
        System.out.println(res);
        return res;
    }

    public Message createAppointment(int tid, int uid, int qid, int blockid) {
        JSONObject obj = new JSONObject();
        obj.put("status", 0);
        obj.put("aid", apps.size() + 1);
        obj.put("suid", uid);
        obj.put("tuid", tid);
        obj.put("bid", blockid);
        apps.add(obj);

        JSONArray temp = uidtoapp.get(uid);
        if (temp == null)
            temp = new JSONArray();
        temp.add(obj);
        uidtoapp.put(uid, temp);

        temp = uidtoapp.get(tid);
        if (temp == null)
            temp = new JSONArray();
        temp.add(obj);
        uidtoapp.put(tid, temp);

        output(apps.toString(), "appointments.json");
        return new Message(200, "ok", null, null);
    } 
    

    // ================
    private final static String filePath = "./webserver/Appointments/";
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
        try {  
            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(str.getBytes());  
            out.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
    }  
}