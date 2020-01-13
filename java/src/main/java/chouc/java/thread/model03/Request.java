package chouc.java.thread.model03;

/**
 * @author chouc
 * @version V1.0
 * @Title: Request
 * @Package chouc.java.thread.model03
 * @Description:
 * @date 1/10/20
 */
public class Request {
    private final String name;
    public Request(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String toString() {
        return "[ Request " + name + " ]";
    }
}