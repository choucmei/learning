package chouc.java.thread.wait02;

/**
 * @author chouc
 * @version V1.0
 * @Title: ThreadA
 * @Package chouc.java.thread.wait02
 * @Description:
 * @date 1/17/20
 */
public class ThreadA extends Thread {

    private Object lock;

    public ThreadA(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        Service service = new Service();
        service.testMethod(lock);
    }

}