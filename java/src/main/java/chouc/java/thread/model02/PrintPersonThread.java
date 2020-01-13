package chouc.java.thread.model02;

/**
 * @author chouc
 * @version V1.0
 * @Title: PrintPersonThread
 * @Package chouc.java.thread.model02
 * @Description:
 * @date 1/10/20
 */
public class PrintPersonThread extends Thread {
    private Person person;
    public PrintPersonThread(Person person) {
        this.person = person;
    }
    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName() + " prints " + person);
        }
    }
}