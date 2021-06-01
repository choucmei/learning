package chouc.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: SelectorClient
 * @Package chouc.java.nio
 * @Description:
 * @date 12/20/19
 */
public class SelectorClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
//        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("sended");
        socketChannel.write(Charset.forName("UTF-8").encode(charBuffer));

        socketChannel.register(selector, SelectionKey.OP_READ);
        selector.select();
        Set<SelectionKey> keySet = selector.selectedKeys();
        Iterator<SelectionKey> it = keySet.iterator();
        while (it.hasNext()) {
            SelectionKey selectionKey = it.next();
            if (selectionKey.isReadable()){
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                socketChannel.read(byteBuffer);
                System.out.println(Charset.forName("UTF-8").decode(byteBuffer).toString());
            }
        }
        socketChannel.close();
    }
}
