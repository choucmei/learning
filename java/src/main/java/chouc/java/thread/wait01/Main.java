package chouc.java.thread.wait01;

/**
 * @author chouc
 * @version V1.0
 * @Title: Main
 * @Package chouc.java.thread.wait01
 * @Description:
 * @date 1/17/20
 *
 *
 * 这也验证了我们刚开始的结论：必须执行完notify()方法所在的synchronized代码块后才释放。
 *
 */
public class Main {

    public static void main(String[] args) {

        try {
            Object lock = new Object();

            ThreadA a = new ThreadA(lock);
            a.start();

            Thread.sleep(50);

            ThreadB b = new ThreadB(lock);
            b.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
