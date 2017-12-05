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
    public String path, type, content;
    private static final int BUFFER_SIZE = 1024;

    public Response(OutputStream output, String type, String path, String content) {
        this.output = output;
        this.type = type;
        this.path = path;
        this.content = content;
    }

    public void response() throws IOException {
        String dir = System.getProperty("user.dir"), str = null; //获取当前的工作目录
        byte[] buffer = new byte[BUFFER_SIZE]; //新建缓冲区
        int ch;
        FileInputStream fis = null;
        if (this.type.equals("static"))
            if (dir != null && path != null) {
                File file = new File(dir, path);
                /*必须添加响应头，否则无法以html格式显示内容*/
                if (file.exists()) {
                    fis = new FileInputStream(file);
                    if (this.path.endsWith("html"))
                        str = "HTTP/1.1 200 OK \r\n" + "Content-Type: text/html\r\n" + "\r\n";
                    if (this.path.endsWith("js"))
                        str = "HTTP/1.1 200 OK \r\n" + "Content-Type: application/javascript\r\n" + "\r\n";
                    if (this.path.endsWith("css"))
                        str = "HTTP/1.1 200 OK \r\n" + "Content-Type: text/css\r\n" + "\r\n" ;
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
        if (this.type.equals("active")) {
            str = "HTTP/1.1 200 OK \r\n" + "Content-Type: application/json;charset=utf-8\r\n" + "\r\n" + this.content;
            System.out.println("23333!" + str);
            output.write(str.getBytes());
        }

        output.close();
    }
}