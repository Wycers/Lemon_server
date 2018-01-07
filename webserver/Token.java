package webserver;

import java.util.HashMap;  
import java.util.Iterator;  
import java.util.Set;  
import java.util.Map.Entry;  

public class Token {  
    MD5 worker = new MD5();
    HashMap<String, Integer> hashMap = new HashMap<String, Integer>(); 
    public int getUser(String token) {
        int res = hashMap.get(token.toUpperCase());
        return res;
    }
    
    public String getToken (String username, int uid) {
        String token = worker.getMD5(username + uid + System.currentTimeMillis());
        hashMap.put(token, uid);
        return token;
    }
} 