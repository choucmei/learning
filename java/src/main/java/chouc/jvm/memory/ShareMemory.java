package chouc.jvm.memory;

import static chouc.jvm.memory.MemoryUtils.UNSAFE;

/**
 * @author 96204
 */
public class ShareMemory {
    public static void main(String[] args) {
        System.out.println("1");
        byte[] b = new byte[4];
        UNSAFE.copyMemory(null, 2692960514112L, b, 16L, 4L);
        System.out.println(new String(b));
    }
}
