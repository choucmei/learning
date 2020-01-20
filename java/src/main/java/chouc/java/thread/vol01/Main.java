package chouc.java.thread.vol01;

/**
 * @author chouc
 * @version V1.0
 * @Title: Main
 * @Package chouc.java.thread.vol01
 * @Description:
 * @date 1/17/20
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        RunThread thread = new RunThread();

        thread.start();
        Thread.sleep(1000);
        thread.setRunning(false);

        System.out.println("已经赋值为false");
    }
}
