package webserver;

import java.io.*;
import java.util.HashMap;

import com.alibaba.fastjson.*;

public class AppointmentAction {
    HashMap<Integer, JSONArray> uidtoapp = new HashMap<>();
    JSONArray apps = null;
    JSONArray status = new JSONArray();

    public AppointmentAction() {
        status = JSON.parseArray(input("status.json"));
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
            return new JSONArray();
        JSONArray res = new JSONArray();
        for (int i = 0, len = origin.size(); i < len; ++i) {
            JSONObject now = origin.getJSONObject(i);
            JSONObject tmp = (JSONObject)status.getJSONObject(now.getInteger("status")).clone();
            tmp.put("s", ua.getUserInfo(now.getInteger("suid")));
            tmp.put("t", ua.getUserInfo(now.getInteger("tuid")));
            tmp.put("b", ba.getBlockInfo(now.getInteger("bid")));
            tmp.put("aid", now.getInteger("aid"));
            res.add(tmp);
        }
        return res;
    }

    public Message createAppointment(int tid, int uid, int qid, int blockid) {
        JSONObject obj = new JSONObject();
        obj.put("status", 1);
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

    public String cancelAppointment(BlockAction ba, int aid) {
        for (int i = 0, len = apps.size(); i < len; ++i) {
            JSONObject now = apps.getJSONObject(i);
            if (now.getInteger("aid") == aid) {
                ba.setTimeblockFree(now.getInteger("bid"));
                now.put("status", 3);
                return "succeeded";
            }
        }
        return "failed";
    }
    
    public void del(JSONArray tmp, String key, int value) {
        for (int i = 0, len = tmp.size(); i < len; ++i) {
            JSONObject now = tmp.getJSONObject(i);
            if (now.getInteger(key) == value) {
                tmp.remove(i);
                return;
            }
        }
    }
    public String delAppointment(int aid) { 
        for (int i = 0, len = apps.size(); i < len; ++i) {
            JSONObject now = apps.getJSONObject(i);
            if (now.getInteger("aid") == aid) {
                del(uidtoapp.get(now.getInteger("suid")), "suid", now.getInteger("suid"));
                del(uidtoapp.get(now.getInteger("tuid")), "tuid", now.getInteger("tuid"));
                apps.remove(i);
                return "succeeded";
            }
        }
        return "failed";
    }
    
    public String confirmAppointment(int aid) {
        for (int i = 0, len = apps.size(); i < len; ++i) {
            JSONObject now = apps.getJSONObject(i);
            if (now.getInteger("aid") == aid) {
                now.put("status", 0);
                return "successed";
            }
        }
        return "failed";
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