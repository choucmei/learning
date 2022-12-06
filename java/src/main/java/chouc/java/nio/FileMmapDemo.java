package chouc.java.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileMmapDemo {

    public static void main(String[] args) {
//        mmapPrivate("/tmp/test.file1");
        System.out.println(Byte.valueOf((byte) 0x88));
    }


   /**
     * 读取文件内容并输出
     *
     * @param fileName 文件名
     */
    public static void mmapWriteFile(String fileName) {

        //向文件中存1M的数据
        int length = 1024;//
        try (FileChannel channel = new RandomAccessFile(fileName, "rw").getChannel();) {

            //一个整数4个字节
            MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, length);
            for (int i = 0; i < length; i++) {
                mapBuffer.put((byte) (Integer.valueOf('a') + i % 26));
            }
            for (int i = 0; i < length; i++) {
                if (i % 50 == 0) System.out.println("");
                //像数组一样访问
                System.out.print((char) mapBuffer.get(i));
            }

            mapBuffer.force();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void mmapPrivate(String fileName) {

        //向文件中存1M的数据
        int length = 1024;//
        try (FileChannel channel = new RandomAccessFile(fileName, "rw").getChannel();) {

            //一个整数4个字节
            MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.PRIVATE, 0, length);
            for (int i = 0; i < length; i++) {
                mapBuffer.put((byte) (Integer.valueOf('a') + i % 26));
            }
            for (int i = 0; i < length; i++) {
                if (i % 50 == 0) System.out.println("");
                //像数组一样访问
                System.out.print((char) mapBuffer.get(i));
            }

            mapBuffer.force();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
