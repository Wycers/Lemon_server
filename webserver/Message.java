package webserver;
public class Message {
    private int status;
    private String message, token;
    private item user = null;
    Message(int sta_, String msg_, item user_, String token) {
        this.status = sta_;
        this.message = msg_;
        this.user = user_;
        this.token = token;
    }
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }
    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }
    /**
     * @return the user
     */
    public item getUser() {
        return user;
    }
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
    /**
     * @param user the user to set
     */
    public void setUser(item user) {
        this.user = user;
    }
}