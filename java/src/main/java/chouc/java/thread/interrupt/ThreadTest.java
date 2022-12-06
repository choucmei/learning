package chouc.java.thread.interrupt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadTest implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new ThreadTest());
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<?> submit = executorService.submit(thread);
        Thread.sleep(1000 * 3);
        submit.cancel(true);
        System.out.println("111111111111111111");
        executorService.shutdown();
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("I am running...");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
