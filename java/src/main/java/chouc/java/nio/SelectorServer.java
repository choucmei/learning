package chouc.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chouc
 * @version V1.0
 * @Title: SelectorServer
 * @Package chouc.java.nio
 * @Description:
 * @date 12/20/19
 */
public class SelectorServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8899));//绑定端口
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select(1000);
            System.out.println(" select ");
            Set<SelectionKey> keySet = selector.selectedKeys();
            System.out.println(" keyset size"+keySet.size());

            Iterator<SelectionKey> iterator = keySet.iterator();

            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                System.out.println(" selected key "+selectionKey.toString());
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannelAddd = (ServerSocketChannel) selectionKey.channel();
                    try {
                        // get socket channel from server socket channel
                        SocketChannel clientChannel = serverSocketChannelAddd.accept();
                        // must to be nonblocking before register with selector
                        clientChannel.configureBlocking(false);
                        // register socket channel to selector with OP_READ
                        clientChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                    } catch (ClosedChannelException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (selectionKey.isReadable()) {
                    SocketChannel readChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    try {
                        readChannel.read(readBuffer);
//                        CharBuffer charBuffer = CharBuffer.allocate(1024);
//                        charBuffer.append("geted");
//                        readChannel.write(Charset.forName("UTF-8").encode(charBuffer));
                        readBuffer.flip();
                        System.out.println(Charset.forName("UTF-8").decode(readBuffer).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                iterator.remove();
            }

//            keySet.forEach(selectionKey -> {
//                System.out.println(" selected key "+selectionKey.toString());
//                if (selectionKey.isAcceptable()) {
//                    ServerSocketChannel serverSocketChannelAddd = (ServerSocketChannel) selectionKey.channel();
//                    try {
//                        // get socket channel from server socket channel
//                        SocketChannel clientChannel = serverSocketChannelAddd.accept();
//                        // must to be nonblocking before register with selector
//                        clientChannel.configureBlocking(false);
//                        // register socket channel to selector with OP_READ
//                        clientChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
//                    } catch (ClosedChannelException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (selectionKey.isReadable()) {
//                    SocketChannel readChannel = (SocketChannel) selectionKey.channel();
//                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
//                    try {
//                        readChannel.read(readBuffer);
//                        CharBuffer charBuffer = CharBuffer.allocate(1024);
//                        charBuffer.append("geted");
//                        readChannel.write(Charset.forName("UTF-8").encode(charBuffer));
//                        readBuffer.flip();
//                        System.out.println(Charset.forName("UTF-8").decode(readBuffer).toString());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                keySet.remove(selectionKey);
//            });
        }
    }
}
