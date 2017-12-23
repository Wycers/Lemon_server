package webserver;

import java.io.*;
import java.util.HashMap;

import com.alibaba.fastjson.*;

public class BlockAction {
    HashMap<Integer, Integer> bidtotid = new HashMap<Integer, Integer>();
    public void work(int tid) {
        JSONArray tbs = JSON.parseArray(input(tid + ".json"));
        for (int i = 0, len = tbs.size(); i < len; ++i) {
            JSONObject tb = tbs.getJSONObject(i);
            bidtotid.put(tb.getInteger("blockid"), tid);
        }
    }

    public BlockAction() {
        work(450);
        System.out.println(getTimeblock(450));
    }


    public JSONArray getTimeblock(int uid) {
        File file = new File(filePath + uid + ".json");
        if (!file.exists()) {
            return new JSONArray();
        }
        return JSON.parseArray(input(uid + ".json"));
    }

    public JSONObject getBlockInfo(int blockid) {
        int tid = queryUid(blockid);
        JSONArray tbs = JSON.parseArray(input(tid + ".json"));
        for (int i = 0, len = tbs.size(); i < len; ++i) {
            JSONObject tb = tbs.getJSONObject(i);
            if (tb.getInteger("blockid") == blockid) 
                return tb;
        }
        return null;
    }

    public void setTimeblockFree(int blockid) {
        int tid = queryUid(blockid);
        JSONObject res = new JSONObject();
        JSONArray tbs = JSON.parseArray(input(tid + ".json"));
        for (int i = 0, len = tbs.size(); i < len; ++i) {
            JSONObject tb = tbs.getJSONObject(i);
            if (tb.getInteger("blockid") == blockid) {
                tb.remove("occupied");
                tbs.set(i, tb);
                break;
            }
        }
        System.out.println(tbs);
        output(tbs.toString(), tid + ".json");
    } 
 
    public void setTimeblock(int blockid, int occupant) {
        int tid = queryUid(blockid);
        JSONObject res = new JSONObject();
        JSONArray tbs = JSON.parseArray(input(tid + ".json"));
        for (int i = 0, len = tbs.size(); i < len; ++i) {
            JSONObject tb = tbs.getJSONObject(i);
            if (tb.getInteger("blockid") == blockid) {
                tb.put("occupied", occupant);
                tbs.set(i, tb);
                break;
            }
        }
        output(tbs.toString(), tid + ".json");
    } 

    public int queryUid(int blockid) {
        return bidtotid.get(blockid);
    }
    

    // ================
    private final static String filePath = "./webserver/Blocks/";
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