package webserver;

import java.io.*;
import java.util.HashMap;

import com.alibaba.fastjson.*;

public class AppointmentAction {
    HashMap<Integer, Integer> bidtotid = new HashMap<Integer, Integer>();

    public void work(int tid) {
        JSONObject tmp = JSON.parseObject(input(tid + ".json"));
        JSONArray tbs = tmp.getJSONArray("timeblocks");
        for (int i = 0, len = tbs.size(); i < len; ++i) {
            JSONObject tb = tbs.getJSONObject(i);
            bidtotid.put(tb.getInteger("blockid"), tid);
        }
    }

    public AppointmentAction() {
        work(450);
    }
    public JSONObject getAppointment(int tid) {
        File file = new File(filePath + tid + ".json");
        if (!file.exists()) {
            JSONObject tmp = JSON.parseObject(input("default.json"));
            tmp.put("uid", tid);
            return tmp;
        }
        System.out.println(JSON.parseObject(input(tid + ".json")));
        return JSON.parseObject(input(tid + ".json"));
    }

    public void setAppointment(int blockid, int uid, Boolean tag) {
        JSONObject res = new JSONObject();
        res.put("uid", uid);
        res.put("temp", tag ? true : false);

        int tid = bidtotid.get(blockid);
        JSONObject tmp = JSON.parseObject(input(tid + ".json"));
        JSONArray tbs = tmp.getJSONArray("timeblocks");
        for (int i = 0, len = tbs.size(); i < len; ++i) {
            JSONObject tb = tbs.getJSONObject(i);
            if (tb.getInteger("blockid") == blockid) {
                tb.put("occupied", res);
                break;
            }
        }
        System.out.println(res);
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