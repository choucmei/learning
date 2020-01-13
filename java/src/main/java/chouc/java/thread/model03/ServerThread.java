package chouc.java.thread.model03;

import java.util.Random;

/**
 * @author chouc
 * @version V1.0
 * @Title: ServerThread
 * @Package chouc.java.thread.model03
 * @Description:
 * @date 1/10/20
 */
//客户端线程不断从请求队列中获取请求，然后处理请求
public class ServerThread extends Thread {
    private Random random;
    private RequestQueue requestQueue;
    public ServerThread(RequestQueue requestQueue, String name, long seed) {
        super(name);
        this.requestQueue = requestQueue;
        this.random = new Random(seed);
    }
    public void run() {
        for (int i = 0; i < 10000; i++) {
            Request request = requestQueue.getRequest();
            System.out.println(Thread.currentThread().getName() + " handles  " + request);
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
            }
        }
    }
}