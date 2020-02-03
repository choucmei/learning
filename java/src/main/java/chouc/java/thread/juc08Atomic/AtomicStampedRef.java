package chouc.java.thread.juc08Atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author chouc
 * @version V1.0
 * @Title: AtomicStampedRef
 * @Package chouc.java.thread.juc08atomic
 * @Description:
 * @date 1/22/20
 */
public class AtomicStampedRef {
    public static void main(String[] args) {
        AtomicStampedReference<Integer> asr = new AtomicStampedReference<>(new Integer(0), 0);

        int[] stamp = new int[1];
        // 调用get方法获取引用对象和对应的版本号
        Integer oldRef = asr.get(stamp);
        // stamp[0]保存版本号
        int oldStamp = stamp[0];
        //尝试以CAS方式更新引用对象，并将版本号+1
        asr.compareAndSet(oldRef, oldRef + 1, oldStamp, oldStamp + 1);
    }
}
