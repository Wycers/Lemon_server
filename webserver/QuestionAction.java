package webserver;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.alibaba.fastjson.*;

public class QuestionAction {
    private int size = 9999;
    HashMap<Integer, JSONObject> qidtoq = new HashMap<>();

    public QuestionAction() {
        File file = new File("./webserver/Questions");
        size = file.list().length;
        System.out.println(size);
        for (int i = 10000; i <= 9999 + size; ++i)
            qidtoq.put(i, JSON.parseObject(input(i + ".json")));
        size += 9999;
    }

    public int createQuestion(String title, String body) {
        ++size;
        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("body", body);
        qidtoq.put(size, obj);

        output(obj.toString(), size + ".json");
        return size;
    }

    public void setQuestion(String title, String body, int qid) {
        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("body", body);
        qidtoq.put(qid, obj);

        output(obj.toString(), qid + ".json");
    }

    public JSONObject getQuestion(int qid) {
        if (qidtoq.get(qid) == null)
            return new JSONObject();
        return qidtoq.get(qid);
    }

    // ================
    private final static String filePath = "./webserver/Questions/";

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