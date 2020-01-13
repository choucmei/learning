package chouc.java.thread.model03;

import java.util.Random;

/**
 * @author chouc
 * @version V1.0
 * @Title: ClientThread
 * @Package chouc.java.thread.model03
 * @Description:
 * @date 1/10/20
 */
//客户端线程不断生成请求，插入请求队列
public class ClientThread extends Thread {
    private Random random;
    private RequestQueue requestQueue;
    public ClientThread(RequestQueue requestQueue, String name, long seed) {
        super(name);
        this.requestQueue = requestQueue;
        this.random = new Random(seed);
    }
    public void run() {
        for (int i = 0; i < 10000; i++) {
            Request request = new Request("No." + i);
            System.out.println(Thread.currentThread().getName() + " requests " + request);
            requestQueue.putRequest(request);
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
            }
        }
    }
}