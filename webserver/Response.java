package webserver;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
    /**
     * 响应并处理请求信息
     */
    public OutputStream output;
    public String filename;
    private static final int BUFFER_SIZE = 1024;

    public Response(OutputStream output, String filename) {
        this.output = output;
        this.filename = filename;
    }

    public void response() throws IOException {
        String path = System.getProperty("user.dir");//获取当前的工作目录
        byte[] buffer = new byte[BUFFER_SIZE];
        int ch;
        FileInputStream fis = null;
        //System.out.println(path+File.separator+filename);
        if (path != null && filename != null) {
            File file = new File(path, filename);
            String str = "";
            /*必须添加响应头，否则无法以html格式显示内容*/
            if (file.exists()) {
                fis = new FileInputStream(file);
                if (filename.endsWith("html"))
                    str = "HTTP/1.1 200 OK \r\n" + "Content-Type: text/html\r\n" + "Set-Cookie: isVisit=true;domain=127.0.0.1;path=/;max-age=1000\r\n" + "\r\n";
                if (filename.endsWith("js"))
                    str = "HTTP/1.1 200 OK \r\n" + "Content-Type: application/javascript\r\n" + "\r\n";
                if (filename.endsWith("css"))
                    str = "HTTP/1.1 200 OK \r\n" + "Content-Type: text/css\r\n" + "\r\n";
                output.write(str.getBytes());
                ch = fis.read(buffer);
                while (ch != -1) {
                    output.write(buffer, 0, ch);
                    ch = fis.read(buffer, 0, BUFFER_SIZE);
                }
            } else {
                str = "HTTP/1.1 404 File Not Found \r\n" + "Content-Type: text/html\r\n" + "Content-Length: 100\r\n"
                        + "\r\n" + "<h1>404 File Not Found!</h1>";
                output.write(str.getBytes());
            }
        }
        output.close();
    }
}