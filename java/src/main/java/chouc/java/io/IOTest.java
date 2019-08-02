package chouc.java.io;

import org.junit.Test;

import java.io.*;

public class IOTest {

    @Test
    public void test01() throws IOException {
        File file = new File("/Users/chouc/Desktop/ticket.html");
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numberRead=0;
        System.out.println(" length:"+buffer.length);
        int count = 0;
        System.out.println(count);
        while ((numberRead = inputStream.read(buffer))!=-1){
            System.out.println(" numberRead : "+numberRead+" .... buffer:"+buffer + "....buffer length :" +buffer.length);
        }
        inputStream.close();
    }

    @Test
    public void test02() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/chouc/Desktop/ticket.html");
//        FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/chouc/Desktop/test03"));
        int tem;
        int count = 0;
        while ((tem = fileInputStream.read())!=-1){
            System.out.println(tem);
            count++;
        }
        System.out.println(" length:"+count);
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
        String tem = "陈佳丽";
        byte[] buffer = tem.getBytes();
        System.out.println(" length:"+buffer.length);
        for (int i = 0;i <buffer.length;i++){
            fileOutputStream.write(buffer[i]);
        }
    }

    //************************test03 和 test 04 分别是两种不同的写入的方法，test03 使用buffer  test04 直接一个一个字节的去写**************************


    public void test05() {

    }
}
