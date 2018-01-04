package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.alibaba.fastjson.*;

// 用于处理请求。
public class Router {
    private String path = null, cookie = null, type = null;
    private JsonObject params = null;
    private Gson gson = null;
    private Object content = null;
    //workers
    private UserAction ua = null;
    private Token ta = null;
    private MenuAction ma = null;
    private TaskAction taska = null;
    private GridAction ga = null;
    private FormAction fa = null;
    private DomainAction da = null;
    private AppointmentAction aa = null;
    private BlockAction ba = null;
    private QuestionAction qa = null;

    Router() {
        ua = new UserAction();
        ta = new Token();
        ma = new MenuAction();
        taska = new TaskAction();
        ga = new GridAction();
        fa = new FormAction();
        da = new DomainAction();
        aa = new AppointmentAction();
        ba = new BlockAction();
        qa = new QuestionAction();
        gson = new Gson();
    }

    public void setArgs(String path, JsonObject params) {
        this.params = params;
        this.path = path;
    }

    public void route() {
        if (this.path.equals("/")) {
            this.type = "static";
            this.path = "/index.html";
        }
        if (this.path.startsWith("/static")) {
            this.type = "static";
        }

        if (this.path.equals("/api/tasks")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            this.content = taska.getTasks(da, uid);
        }

        if (this.path.equals("/api/login")) {
            this.type = "POST";
            System.out.println(this.params);
            this.content = ua.verify(this.ta, this.params);
        }

        if (this.path.equals("/api/menu")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            this.content = ma.getMenu(this.da.getDomains(uid), type);
        }

        if (this.path.equals("/api/users/grid")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            if (type == 0) {
                this.content = ga.getGrid("users");
            }
        }

        if (this.path.equals("/api/users/form/modify")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int toEdit = params.get("id").getAsInt();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            //if (type == 0) {
            this.content = fa.getForms(this.ua, "users:modify", toEdit);
            //}
        }

        if (this.path.equals("/api/users/form/create")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            if (type == 0) {
                this.content = fa.getForms("users:create");
            }
        }
        if (this.path.equals("/api/users")) {
            this.type = "GET";
            /*String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            if (type == 0) {
                this.content = fa.getForms("users");
            }*/
            this.content = ua.getList(params);
        }
        if (this.path.equals("/api/users/create")) {
            this.type = "GET";
            String name = params.get("name").getAsString();
            int type = params.get("type").getAsInt();
            String username = params.get("username").getAsString();
            System.out.println(username);
            this.content = ua.addUser(username, type, name);
        }
        if (this.path.equals("/api/domain/grid")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            this.content = ga.getGrid("domain");
        }
        if (this.path.equals("/api/domain/get")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            System.out.println(params);
            int did = params.get("domainid").getAsInt();
            this.content = da.getDomain(did);
        }
        if (this.path.equals("/api/domain/form/add")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            this.content = ga.getGrid("domainAdd");
        }
        if (this.path.equals("/api/domain/query")) {
            this.type = "GET";
            int uid = params.get("uid").getAsInt();
            this.content = this.ua.queryUser(uid);
        }
        if (this.path.equals("/api/domain/create")) {
            this.type = "POST";
            String token = params.get("token").getAsString();
            JSONObject params_ = JSON.parseObject(params.toString());
            String title = params_.getString("title");
            String etitle = params_.getString("etitle");
            if (title == null)
                return;
            if (etitle == null)
                return;
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            JSONArray users = params_.getJSONArray("users");
            this.content = da.addDomain(title, etitle, users);
        }
        if (this.path.equals("/api/domain/edit")) {
            this.type = "POST";
            String token = params.get("token").getAsString();
            JSONObject params_ = JSON.parseObject(params.toString());
            String title = params_.getString("title");
            String etitle = params_.getString("etitle");
            if (title == null)
                return;
            if (etitle == null)
                return;
            int uid = this.ta.getUser(token);
            int type = this.ua.getType(uid);
            int domainid = params_.getInteger("domainid");
            JSONArray users = params_.getJSONArray("users");
            this.content = da.editDomain(domainid, title, etitle, users);
        }
        if (this.path.equals("/api/domainDetail")) {
            this.type = "GET";
            System.out.println(params);
            int did = params.get("id").getAsInt();
            this.content = da.getDomainDetail(did);
        }
        if (this.path.equals("/api/score")) {
            this.type = "GET";
            System.out.println(params);
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int did = params.get("id").getAsInt();
            this.content = ua.getScore(uid, did);
        }
        if (this.path.equals("/api/appointment/form")) {
            this.type = "GET";
            System.out.println(params);
            //String token = params.get("token").getAsString();
            //int uid = this.ta.getUser(token);
            this.content = fa.getForms("appointment");
        }
        if (this.path.equals("/api/user")) {
            this.type = "GET";
            int uid = this.params.get("uid").getAsInt();
            this.content = ua.getUser(uid);
        }
        if (this.path.equals("/api/timeblock/query")) {
            this.type = "GET";
            int uid = this.params.get("uid").getAsInt();
            System.out.println(uid);
            System.out.println(ba.getTimeblock(uid));
            this.content = ba.getTimeblock(uid);
        }
        if (this.path.equals("/api/appointment/create")) {
            this.type = "POST";
            System.out.println(params);
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            try {
                int blockid = this.params.get("select").getAsInt();
            } catch (Exception e) {
                this.content = new Message(403, "invalid timeblock", null, null);
                return;
            }
            int blockid = this.params.get("select").getAsInt();
            String body = this.params.get("body").getAsString();
            String title = this.params.get("title").getAsString();

            ba.setTimeblock(blockid, uid);

            int qid = qa.createQuestion(title, body);
            int tid = ba.queryUid(blockid);
            this.content = aa.createAppointment(tid, uid, qid, blockid);
        }
        if (this.path.equals("/api/appointment/menu")) {
            this.type = "GET";
            this.content = ma.getMenu("appointment");
        }
        if (this.path.equals("/api/appointment/query")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            this.content = aa.getAppointment(ua, ba, uid);
        }
        if (this.path.equals("/api/appointment/cancel")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int aid = params.get("aid").getAsInt();
            this.content = aa.cancelAppointment(ba, aid);
        }
        if (this.path.equals("/api/appointment/confirm")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int aid = params.get("aid").getAsInt();
            this.content = aa.confirmAppointment(aid);
        }
        if (this.path.equals("/api/appointment/cancel/hint")) {
            this.type = "GET";
            this.content = "你确定要取消这个预约吗";
            System.out.println(this.params);
        }
        if (this.path.equals("/api/appointment/del")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int aid = params.get("aid").getAsInt();
            this.content = aa.delAppointment(aid);
        }
        if (this.path.equals("/api/appointment/del/hint")) {
            this.type = "GET";
            this.content = "你确定要删除这个预约吗";
            System.out.println(this.params);
        }
        if (this.path.equals("/api/question/get")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            int qid = params.get("qid").getAsInt();
            this.content = qa.getQuestion(qid);
        }
        if (this.path.equals("OPTIONS")) {
            this.type = "OPTIONS";
        }
        if (this.path.equals("/api/settings/form")) {
            this.type = "GET";
            String token = params.get("token").getAsString();
            int uid = this.ta.getUser(token);
            this.content = fa.getForms(ua, "settings", uid);
        }
        if (this.path.equals("/api/upload")) {
            this.type = "POST";
            this.content = "HTTP/2.0 200 OK\r\n\r\n";
        }
    }

    public String getType() {
        return this.type;
    }

    public String getPath() {
        return this.path;
    }

    public Object getContent() {
        return this.content;
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