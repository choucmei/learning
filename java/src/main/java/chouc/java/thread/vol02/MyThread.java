package chouc.java.thread.vol02;

import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: MyThread
 * @Package chouc.java.thread.vol02
 * @Description:
 * @date 1/17/20
 *
 *
 *
 * 证明 volatile 不是原子操作
 *
 *
 */
public class MyThread extends Thread {
    volatile public static int count;

    private static void addCount() {
        for (int i = 0; i < 10000; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count = count + 1;
            System.out.println(Thread.currentThread().getName() + "  v:" + count);
        }

    }

    @Override
    public void run() {
        addCount();
    }
}