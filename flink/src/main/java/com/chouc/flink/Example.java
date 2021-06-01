package com.chouc.flink;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author chouc
 * @version V1.0
 * @Title: Example
 * @Package com.chouc.flink
 * @Description:
 * @date 2021/5/25
 */
public class Example {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket(8888);
        Socket socket =server.accept();
        System.out.println("connect");
        BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
        String[] ids = new String[]{"abb2305a-6f7a-4925-8ae5-d5b7f64a2e65", "8723e15a-2549-41e5-a061-c37d739e96d9", "8723e15a-2549-41e5-a061-c37d739e96d9"};
        for (int i=0;i<1000;i++) {
            String outString = ids[i % 2]+","+(System.currentTimeMillis() + 1000 * i % 10)+","+"36.0";
            System.out.println(outString);
            output.write(outString.getBytes());
            output.flush();
            Thread.sleep(1000);
        }
    }
}
