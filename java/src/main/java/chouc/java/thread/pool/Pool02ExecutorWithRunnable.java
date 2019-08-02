package chouc.java.thread.pool;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Pool02ExecutorWithRunnable {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            public void run() {
                Random random = new Random();
                try {
                    System.out.println("start");
                    Thread.sleep(random.nextInt(10)*100);
                    System.out.println("shutdown");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
//        executorService.shutdown();

        ExecutorService executorService2 = Executors.newCachedThreadPool();
        executorService2.execute(new Runnable() {
            public void run() {
                Random random = new Random();
                try {
                    System.out.println("start 1");
                    Thread.sleep(random.nextInt(10)*100);
                    System.out.println("shutdown");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService2.shutdown();
    }
}
