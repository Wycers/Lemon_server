package webserver; 
import com.alibaba.fastjson.*;
public class Domain {
    private int domainid;
    private JSONObject content;
    public int getDomainId() {
        return this.domainid;
    }
    public JSONObject getContent() {
        return this.content;
    }
    public void setDomainId(int did) {
        this.domainid = did;
    }
    public void setContent(JSONObject content) {
        this.content = content;
    }
}