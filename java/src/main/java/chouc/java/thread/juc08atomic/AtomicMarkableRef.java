package chouc.java.thread.juc08atomic;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author chouc
 * @version V1.0
 * @Title: AtomicMarkableRef
 * @Package chouc.java.thread.juc08atomic
 * @Description:
 * @date 1/22/20
 */
public class AtomicMarkableRef {
    public static void main(String[] args) {
        AtomicMarkableReference<Integer> atomicMarkableReference = new AtomicMarkableReference<>(new Integer(0), true);
        boolean[] old = new boolean[1];
        Integer oldValue = atomicMarkableReference.get(old);
        atomicMarkableReference.compareAndSet(oldValue,oldValue+1,old[0],old[0]);
    }
}
