package chouc.java.thread.juc08Atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author chouc
 * @version V1.0
 * @Title: AtomicRefTest
 * @Package chouc.java.thread.juc08atomic
 * @Description:
 * @date 1/22/20
 */
public class AtomicRefTest {
    public static void main(String[] args) {
        AtomicReference atomicReference = new AtomicReference(new Integer(0));
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AtomicRefThread atomicIntegerThread = new AtomicRefThread(atomicReference);
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
        System.out.println(atomicReference.get());
    }
}

class AtomicRefThread extends Thread {

    AtomicReference<Integer> atomicReference;

    public AtomicRefThread(AtomicReference<Integer> atomicReference) {
        this.atomicReference = atomicReference;
    }

    @Override
    public void run() {
        Integer integer = atomicReference.get();
        atomicReference.compareAndSet(integer,integer + 1);
    }
}
