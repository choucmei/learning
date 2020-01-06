package chouc.java.io;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IOTest {

    @Test
    public void test01() throws IOException {
        File file = new File("/Users/chouc/Desktop/产线.info");
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numberRead = 0;
        System.out.println(" length:" + buffer.length);
        int count = 0;
        System.out.println(count);
        while ((numberRead = inputStream.read(buffer)) != -1) {
            System.out.println(" numberRead : " + numberRead + " .... buffer:" + new String(buffer) + "....buffer length :" + buffer.length);
        }
        inputStream.close();
    }

    @Test
    public void test02() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/chouc/Desktop/产线.info");
//        FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/chouc/Desktop/test03"));
        int tem;
        int count = 0;
        while ((tem = fileInputStream.read()) != -1) {
            System.out.println((char) tem);
            count++;
        }
        System.out.println(" length:" + count);
    }

    //************************test01 和 test 02 分别是两种不同的取法，test01 使用buffer  test02 直接一个一个字符的去读**************************


    @Test
    public void test03() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/chouc/Desktop/test03"));
        OutputStream outputStream = fileOutputStream;
        String tem = "cjl";
        outputStream.write(tem.getBytes());
        outputStream.close();
    }


    @Test
    public void test04() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/chouc/Desktop/test04"));
        String tem = "cjl";
        byte[] buffer = tem.getBytes();
        System.out.println(" length:" + buffer.length);
        for (int i = 0; i < buffer.length; i++) {
            fileOutputStream.write(buffer[i]);
        }
    }

    //************************test03 和 test 04 分别是两种不同的写入的方法，test03 使用buffer  test04 直接一个一个字节的去写**************************


    @Test
    public void test05() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/chouc/Desktop/产线.info");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        // BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/chouc/Desktop/产线.info"));
//        String line = null;
//        while ((line = bufferedReader.readLine()) != null) {
//            System.out.println(line);
//        }
        int t;
        while ((t = bufferedReader.read()) != -1) {
            System.out.println((char) t);
            break;
        }
    }

    @Test
    public void test06() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/chouc/Desktop/产线.info");
        FileChannel channel = fileInputStream.getChannel();
        System.out.println("max :"
                + Runtime.getRuntime().maxMemory() / 1024);

        System.out.println("total :" + Runtime.getRuntime().totalMemory() / 1024);

        System.out.println("before :"
                + Runtime.getRuntime().freeMemory() / 1024);
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024 * 1024);
        System.out.println("after :"
                + Runtime.getRuntime().freeMemory() / 1024);
        ByteBuffer heapBuffer = ByteBuffer.allocate(1024 * 1024 * 5);
        System.out.println("after :"
                + Runtime.getRuntime().freeMemory() / 1024);
        while (channel.read(directBuffer) != -1) {
            System.out.println(new String(directBuffer.array()));
            break;
        }


    }


    @Test
    public void testReader() {
        File file = new File("/Users/chouc/Desktop/产线.info");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
//            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


}
