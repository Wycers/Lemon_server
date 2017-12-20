package webserver;
public class Message {
    private int status;
    private String message, token;
    private User user = null;
    Message(int sta_, String msg_, User user_, String token) {
        this.status = sta_;
        this.message = msg_;
        this.user = user_;
        this.token = token;
    }
}