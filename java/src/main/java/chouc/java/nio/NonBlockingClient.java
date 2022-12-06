package chouc.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class NonBlockingClient {
    static SocketChannel socketChannel = null;
    static Selector selector = null;

    public static void main(String[] args) throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
        socketChannel.configureBlocking(false);
        selector = Selector.open();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteBuffer buf = ByteBuffer.allocate(1024);
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()) {
                    String inputStr = scanner.next();
                    buf.put(inputStr.getBytes());
                    buf.flip();
                    try {
                        socketChannel.write(buf);
                        while (buf.remaining() > 0) {
                            socketChannel.write(buf);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    buf.clear();
                }
                scanner.close();
            }
        }).start();

        socketChannel.register(selector, SelectionKey.OP_READ);
        int result = 0;
        int i = 1;
        while ((result = selector.select()) > 0) {
            System.out.println(String.format("selector %dth loop, ready event number is %d", i++, result));
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey sk = iterator.next();

                if (sk.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) sk.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = socketChannel.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                }
                iterator.remove();
            }
        }
        socketChannel.close();
    }
}



