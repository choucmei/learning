package chouc.java.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @author chouc
 * @version V1.0
 * @Title: FileChannelTest02
 * @Package chouc.java.nio
 * @Description:
 * @date 11/5/19
 */
public class FileChannelTest02 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/chouc/Desktop/产线.info");
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        fileChannel.read(byteBuffer);

        byteBuffer.flip();
//
//        StringBuffer sb = new StringBuffer();
//        while (byteBuffer.remaining() > 0){
//            byte b = byteBuffer.get();
//            sb.append((char)b);
//        }
//        System.out.println(sb);
        CharBuffer result = Charset.forName("UTF-8").decode(byteBuffer);
        System.out.println(result.toString());

        result.append("ll");
        fileChannel.write(Charset.forName("UTF-8").encode(result));

        fileChannel.close();
        fileInputStream.close();

    }
}
