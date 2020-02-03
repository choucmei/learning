package chouc.java.thread.juc09AtomicIntegerArray;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author chouc
 * @version V1.0
 * @Title: AtomicIntegerArrayTest
 * @Package chouc.java.thread.juc09atomicarray
 * @Description:
 * @date 1/22/20
 */
public class AtomicIntegerArrayTest {
    public static void main(String[] args) {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        atomicIntegerArray.incrementAndGet(0);
        atomicIntegerArray.incrementAndGet(1);
        System.out.println(atomicIntegerArray.get(0));

    }
}
