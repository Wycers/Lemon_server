package webserver;
public class Task {
    private int id;
    private String item, eitem, content, ddl, color;
    Task() {}
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param item the item to set
     */
    public void setItem(String item) {
        this.item = item;
    }
    /**
     * @return the item
     */
    public String getItem() {
        return item;
    }
    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }
    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }
    /**
     * @param ddl the ddl to set
     */
    public void setDdl(String ddl) {
        this.ddl = ddl;
    }
    /**
     * @return the ddl
     */
    public String getDdl() {
        return ddl;
    }
    /**
     * @param eitem the eitem to set
     */
    public void setEitem(String eitem) {
        this.eitem = eitem;
    }
    /**
     * @return the eitem
     */
    public String getEitem() {
        return eitem;
    }
}