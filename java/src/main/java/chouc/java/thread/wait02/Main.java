package chouc.java.thread.wait02;

/**
 * @author chouc
 * @version V1.0
 * @Title: Main
 * @Package chouc.java.thread.wait02
 * @Description:
 * @date 1/17/20
 */
public class Main {
    public static void main(String[] args) {

        try {
            Object lock = new Object();

            ThreadA a = new ThreadA(lock);
            a.start();

            Thread.sleep(5000);
            System.out.println(" start interrupt");
            a.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
