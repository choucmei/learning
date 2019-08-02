package chouc.java.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(8080,10);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        TransBean transBean = (TransBean) objectInputStream.readObject();
        transBean.invok();

        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(transBean);
        objectOutputStream.flush();

        objectInputStream.close();
        objectOutputStream.close();
        socket.close();
        serverSocket.close();
    }
}
