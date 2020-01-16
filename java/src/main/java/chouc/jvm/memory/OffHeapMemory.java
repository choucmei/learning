package chouc.jvm.memory;

import org.junit.Test;
import sun.misc.Unsafe;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: OffHeapMemoryNIO
 * @Package chouc.jvm.memory
 * @Description:
 *
 * -verbose:gc -XX:+PrintGCDetails -XX:MaxDireactMemorySize
 *
 * -verbose:gc -XX:+PrintGCDetails -XX:MaxDireactMemorySize  -XX:+DisableExplicitGC
 *
 * @date 1/15/20
 */
public class OffHeapMemory {

    @Test
    public void nio() throws InterruptedException {
        for (int i = 1; i < 10000; i++) {
            System.out.println(i);
            ByteBuffer.allocateDirect(10 * 1024 * 1024);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static Unsafe unsafe = Unsafe.getUnsafe();

    @Test
    public void unsafe() throws InterruptedException {
        for (int i = 1; i < 10000; i++) {
            System.out.println(i);
            unsafe.allocateMemory(10 * 1024 * 1024);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
