package chouc.java.thread.juc11longadder;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author chouc
 * @version V1.0
 * @Title: LongAdderTest
 * @Package chouc.java.thread.juc11longadder
 * @Description:
 * @date 1/22/20
 */
public class LongAdderTest {
    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();
        longAdder.increment();
        longAdder.sum();
    }
}
