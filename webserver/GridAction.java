package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import com.alibaba.fastjson.*;

// 用于处理请求。
public class GridAction {
    // Basic Things
    JSONObject users, domain, domainAdd;

    GridAction() {
        this.users = JSON.parseObject(input("manage.json"));
        this.domain = JSON.parseObject(input("domain.json"));
        this.domainAdd = JSON.parseObject(input("domain_add.json"));
    }

    //Particular Things
    public JSONObject getGrid(String tag) {
        if (tag.equals("users"))
            return this.users;
        if (tag.equals("domain"))
            return this.domain;
        if (tag.equals("domainAdd"))
            return this.domainAdd;
        return null;
    }

    //----General Things----
    private final static String filePath = "./webserver/Grids/";

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