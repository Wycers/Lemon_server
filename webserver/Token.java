package webserver;

import java.util.HashMap;  
import java.util.Iterator;  
import java.util.Set;  
import java.util.Map.Entry;  
import java.security.MessageDigest; 

public class Token {  
  
    HashMap<String, Integer> hashMap = new HashMap<String, Integer>(); 
    public int getUser(String token) {
        int res = hashMap.get(token.toUpperCase());
        return res;
    }
    
    public String getToken (String username, int uid) {
        String token = getMD5(username + uid + System.currentTimeMillis());
        hashMap.put(token, uid);
        System.out.println(token + ":" + uid);
        return token;
    }

    public static String getMD5(String message) {  
        String md5str = "";  
        try {  
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象  
            MessageDigest md = MessageDigest.getInstance("MD5");  
      
            // 2 将消息变成byte数组  
            byte[] input = message.getBytes();  
      
            // 3 计算后获得字节数组,这就是那128位了  
            byte[] buff = md.digest(input);  
      
            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串  
            md5str = bytesToHex(buff);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return md5str;  
    }
    public static String bytesToHex(byte[] bytes) {  
        StringBuffer md5str = new StringBuffer();  
        // 把数组每一字节换成16进制连成md5字符串  
        int digital;  
        for (int i = 0; i < bytes.length; i++) {  
            digital = bytes[i];  
            if (digital < 0)
                digital += 256;  
            if (digital < 16) 
                md5str.append("0");  
            md5str.append(Integer.toHexString(digital));  
        }  
        return md5str.toString().toUpperCase();  
    }  
} 