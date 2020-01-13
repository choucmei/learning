package chouc.java.thread.model01;

/**
 * @author chouc
 * @version V1.0
 * @Title: UserThread
 * @Package chouc.java.thread.review.example01
 * @Description:
 * @date 1/9/20
 */
public class UserThread extends Thread {
    private final Gate gate;
    private final String myname;
    private final String myaddress;
    public UserThread(Gate gate, String myname, String myaddress) {
        this.gate = gate;
        this.myname = myname;
        this.myaddress = myaddress;
    }
    public void run() {
        System.out.println(myname + " BEGIN");
        while (true) {
            gate.pass(myname, myaddress);
        }
    }
}