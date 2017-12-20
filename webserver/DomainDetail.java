package webserver;

import java.util.List;
import com.alibaba.fastjson.*;

public class DomainDetail {
    private int domainid;
    JSONArray lecturer, assistant, administrator, students;

    public void setDomainId(int domainid) {
        this.domainid = domainid;
    }
    public void setLecturer(JSONArray lecturer) {
        this.lecturer = lecturer;
    }
    public void setStudents(JSONArray students) {
        this.students = students;
    }
    public void setAdministrator(JSONArray administrator) {
        this.administrator = administrator;
    }
    public void setAssistant(JSONArray assistant) {
        this.assistant = assistant;
    }
    public int getDomainId() {
        return this.domainid;
    }
    public JSONArray getLecturer() {
        return this.lecturer;
    }
    public JSONArray getStudents() {
        return this.students;
    }
    public JSONArray getAdministrator() {
        return this.administrator;
    }
    public JSONArray getAssistant() {
        return this.assistant;
    }
}