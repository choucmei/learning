package chouc.java.thread.wait02;

/**
 * @author chouc
 * @version V1.0
 * @Title: Service
 * @Package chouc.java.thread.wait02
 * @Description:
 * @date 1/17/20
 */
public class Service {
    public void testMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("begin wait()");
                lock.wait();
                System.out.println("  end wait()");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("出现异常了，因为呈wait状态的线程被interrupt了！");
        }
    }
}