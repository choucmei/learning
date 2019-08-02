package chouc.java.socket;

import java.io.*;
import java.net.Socket;

public class SocketClientTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost",8080);
        OutputStream outputStream = socket.getOutputStream();
        TransBean<Integer> transBean = new TransBean("chouc.java.proxy.impl.Boss","yifu",new Object[]{"1"},new Class[]{String.class});
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(transBean);
        outputStream.flush();

        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        transBean = (TransBean) objectInputStream.readObject();


        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
        System.out.println("result:"+transBean.getResult());
    }
}
