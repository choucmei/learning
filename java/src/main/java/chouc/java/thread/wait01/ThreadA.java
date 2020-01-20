package chouc.java.thread.wait01;

/**
 * @author chouc
 * @version V1.0
 * @Title: ThreadA
 * @Package chouc.java.thread.wait01
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
        try {
            synchronized (lock) {
                if (MyList.size() != 5) {
                    System.out.println("wait begin "
                            + System.currentTimeMillis());
                    lock.wait();
                    System.out.println("wait end  "
                            + System.currentTimeMillis());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}