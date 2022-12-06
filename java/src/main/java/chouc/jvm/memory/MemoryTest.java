package chouc.jvm.memory;

import java.nio.ByteBuffer;

import static chouc.jvm.memory.MemoryUtils.UNSAFE;

public class MemoryTest {
    public static void main(String[] args) {
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 5);
//        MemoryTest o = new MemoryTest();
//        MemorySegment memorySegment = new MemorySegment(byteBuffer, o);
//        test1(memorySegment);

        byte[] sourceData = "hhhh".getBytes();
        byte[] sourceData2 = "xxxx".getBytes();
        byte[] tgtData = new byte[sourceData.length * 2];

        final long arrayAddress = UNSAFE.arrayBaseOffset(byte[].class);
        long address = UNSAFE.allocateMemory(4);
        long nAddre = address;

        // heap to offheap
        UNSAFE.copyMemory(sourceData, arrayAddress, null, nAddre, 4);
        UNSAFE.copyMemory(sourceData2, arrayAddress, null, nAddre += 4, 4);
        UNSAFE.copyMemory(sourceData2, arrayAddress, null, nAddre += 4, 4);


        // offheap to heap
        UNSAFE.copyMemory(null, address, tgtData, arrayAddress, 4);

        System.out.println(new String(tgtData));


        // offheap to heap
        UNSAFE.copyMemory(null, address, tgtData, arrayAddress, nAddre - address + 100);

        System.out.println(new String(tgtData));

    }

    public static void test1(MemorySegment memorySegment) {

        byte[] sourceData = "hhhh".getBytes();
        memorySegment.put(0, sourceData);

        byte[] sourceData2 = "xxxx".getBytes();
        memorySegment.put(0, sourceData2);

        byte[] targetData = new byte[sourceData.length];
        memorySegment.get(0, targetData);
        String value = new String(targetData);
        System.out.println(value);

        memorySegment.get(0, targetData);
        String value2 = new String(targetData);
        System.out.println(value2);
    }


    public static void test2(MemorySegment memorySegment) {
        String data = "hhhh";
        byte[] sourceData = data.getBytes();
        ByteBuffer allocate = ByteBuffer.allocateDirect(sourceData.length * 2);
        System.out.println(MemorySegment.getByteBufferAddress(allocate));
        System.out.println(allocate);
        allocate.put(sourceData);
        System.out.println(allocate);
        allocate.flip();
        System.out.println(allocate);
        memorySegment.put(0, allocate, sourceData.length);
        System.out.println(allocate);


        System.out.println("-----------------------------------");

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(sourceData.length * 2);
        System.out.println(byteBuffer);
        memorySegment.get(0, byteBuffer, sourceData.length);
        System.out.println(byteBuffer);

        byteBuffer.flip();
        System.out.println(byteBuffer);

        byte[] targetData = new byte[sourceData.length];
        byteBuffer.get(targetData);
        System.out.println(new String(targetData));
        System.out.println(byteBuffer);


    }


}
