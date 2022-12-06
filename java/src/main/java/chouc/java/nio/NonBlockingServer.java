package chouc.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NonBlockingServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8888));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  //首先注册ACCEPT事件

        int result = 0;
        int i = 1;
        while (true) {  //遍历获得就绪事件
            result = selector.select();
            System.out.println(String.format("selector %dth loop, ready event number is %d", i++, result));
            if (result == 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {  //就绪事件可能不止一个
                SelectionKey sk = iterator.next();

                if (sk.isAcceptable()) {  //如果是ACCEPT，那么与之关联的channel肯定是个ServerSocketChannel
                    System.out.println("服务端有ACCEPT事件就绪");
                    ServerSocketChannel ss = (ServerSocketChannel) sk.channel();
                    SocketChannel socketChannel = ss.accept();
                    socketChannel.configureBlocking(false);  //也切换非阻塞
                    socketChannel.register(selector, SelectionKey.OP_READ);  //注册read事件
                } else if (sk.isReadable()) {    //如果是READ，那么与之关联的channel肯定是个SocketChannel
                    System.out.println("服务端有READ事件就绪");
                    SocketChannel socketChannel = (SocketChannel) sk.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int len = 0;
                    StringBuilder sb = new StringBuilder();
                    while ((len = socketChannel.read(buf)) > 0) {
                        buf.flip();
                        String s = new String(buf.array(), 0, len);
                        sb.append(s);
                        buf.clear();
                    }
                    //服务端开始响应消息
                    ByteBuffer readAtta = (ByteBuffer) sk.attachment();
                    if (readAtta != null) {
                        System.out.println("lasttime readAtta string is: " + new String(readAtta.array()));
                    } else {
                        System.out.println("lasttime readAtta is null ");
                    }
                    sk.attach(ByteBuffer.wrap(sb.toString().getBytes()));
                    sk.interestOps(sk.interestOps() | SelectionKey.OP_WRITE);
                    String sendStr = "您的消息'" + sb.toString() + "'我已经收到了";
                    System.out.println("接下来attach的是：" + sendStr);
                    sk.attach(ByteBuffer.wrap(sendStr.getBytes()));
                } else if (sk.isWritable()) {
                    System.out.println("服务端有WRITE事件就绪");
                    SocketChannel socketChannel = (SocketChannel) sk.channel();

                    ByteBuffer writeAtta = (ByteBuffer) sk.attachment();
                    if (writeAtta != null) {
                        System.out.println("lasttime writeAtta string is: " + new String(writeAtta.array()));
                    } else {
                        System.out.println("lasttime writeAtta is null ");
                    }

                    sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
                }
                iterator.remove();
                System.out.println("after remove key");
            }
        }
    }
}



