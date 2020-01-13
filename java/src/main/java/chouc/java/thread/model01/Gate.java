package chouc.java.thread.model01;

/**
 * @author chouc
 * @version V1.0
 * @Title: Gate
 * @Package chouc.java.thread.review.example01
 * @Description:
 * @date 1/9/20
 */
public class Gate {
    private int counter = 0;
    private String name = "Nobody";
    private String address = "Nowhere";

    public void pass(String name, String address) {
        this.counter++;
        this.name = name;
        this.address = address;
        check();
    }
    private void check() {
        if (name.charAt(0) != address.charAt(0)) {
            System.out.println("***** BROKEN ***** " + toString());
        }
    }
    public String toString() {
        return "No." + counter + ": " + name + ", " + address;
    }
}