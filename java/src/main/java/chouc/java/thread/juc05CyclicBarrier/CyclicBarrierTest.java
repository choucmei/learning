package chouc.java.thread.juc05CyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: CyclicBarrierTest
 * @Package chouc.java.thread.juc05
 * @Description:
 * @date 1/21/20
 */
public class CyclicBarrierTest {
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println(" Runnable of CyclicBarrier working ");
            }
        });
        CyclicBarrierWorkThread cyclicBarrierWorkThread01 = new CyclicBarrierWorkThread(cyclicBarrier);
        CyclicBarrierWorkThread cyclicBarrierWorkThread02 = new CyclicBarrierWorkThread(cyclicBarrier);
        CyclicBarrierWorkThread cyclicBarrierWorkThread03 = new CyclicBarrierWorkThread(cyclicBarrier);
        cyclicBarrierWorkThread01.setName("cyclicBarrierWorkThread01");
        cyclicBarrierWorkThread02.setName("cyclicBarrierWorkThread02");
        cyclicBarrierWorkThread03.setName("cyclicBarrierWorkThread03");
        cyclicBarrierWorkThread01.start();
        cyclicBarrierWorkThread02.start();
        cyclicBarrierWorkThread03.start();
//        cyclicBarrierWorkThread03.interrupt();
        TimeUnit.SECONDS.sleep(10);
        System.out.println("isBroken：" + cyclicBarrier.isBroken());
    }
}

class CyclicBarrierWorkThread extends Thread {

    private CyclicBarrier cyclicBarrier;

    public CyclicBarrierWorkThread(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " run ");
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " InterruptedException");
        } catch (BrokenBarrierException e) {
            System.out.println(Thread.currentThread().getName() + " BrokenBarrierException");
        }
        System.out.println(Thread.currentThread().getName() + " work ");
    }
}
