package webserver;
public class Result {
    int currentPage, lastPage, perPage, total;
    Object data;
    Result() {}
    Result(int cPage, int lPage, int pPage, int total, Object data) {
        this.currentPage = cPage;
        this.lastPage = lPage;
        this.perPage = pPage;
        this.total = total;
        this.data = data;
    }
    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }
    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }
    /**
     * @return the lastPage
     */
    public int getLastPage() {
        return lastPage;
    }
    /**
     * @return the perPage
     */
    public int getPerPage() {
        return perPage;
    }
    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }
    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    /**
     * @param data the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }
    /**
     * @param lastPage the lastPage to set
     */
    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
    /**
     * @param perPage the perPage to set
     */
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }
}