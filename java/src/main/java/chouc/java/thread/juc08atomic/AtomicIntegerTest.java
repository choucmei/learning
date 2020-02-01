package chouc.java.thread.juc08atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chouc
 * @version V1.0
 * @Title: AtomicIntegerTest
 * @Package chouc.java.thread.juc08atomic
 * @Description:
 * @date 1/22/20
 */
public class AtomicIntegerTest {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AtomicIntegerThread atomicIntegerThread = new AtomicIntegerThread(atomicInteger);
            atomicIntegerThread.start();
            list.add(atomicIntegerThread);
        }
        for (Thread t : list) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(atomicInteger.get());
        ReentrantLock reentrantLock = new ReentrantLock();
        AtomicIntegerThread02 atomicIntegerThread02 = new AtomicIntegerThread02(atomicInteger,reentrantLock);
        atomicIntegerThread02.start();
        try {
            atomicIntegerThread02.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(atomicInteger.get());
    }

}

class AtomicIntegerThread extends Thread {
    AtomicInteger atomicInteger;

    public AtomicIntegerThread(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            atomicInteger.incrementAndGet();
        }
    }
}

class AtomicIntegerThread02 extends Thread {
    AtomicInteger atomicInteger;
    ReentrantLock reentrantLock;

    public AtomicIntegerThread02(AtomicInteger atomicInteger,ReentrantLock reentrantLock) {
        this.atomicInteger = atomicInteger;
        this.reentrantLock = reentrantLock;
    }

    @Override
    public void run() {
        reentrantLock.lock();
        try {
            atomicInteger.set(10);
        } finally {
            reentrantLock.unlock();
        }

    }
}