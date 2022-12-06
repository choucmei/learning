package chouc.java.thread;

import java.util.concurrent.*;

public class T implements Callable<String> {

    CountDownLatch countDownLatch;
    private String name;
    private int times;

    public T(CountDownLatch countDownLatch, String name, int times) {
        this.countDownLatch = countDownLatch;
        this.name = name;
        this.times = times;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(times * 1000);
        countDownLatch.countDown();
        System.out.println(name + " finnish");
        return name + " result: finnish";
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        T t1 = new T(countDownLatch, "1", 30);
        T t2 = new T(countDownLatch, "2", 1);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> submit1 = executorService.submit(t1);
        Future<String> submit2 = executorService.submit(t2);
        countDownLatch.await();
        while (!submit1.isDone() && !submit2.isDone()) {
            Thread.sleep(1000 * 1);
        }
        String rs = "";
        if (submit1.isDone()) {
            submit2.cancel(true);
            rs = submit1.get();
        } else {
            submit1.cancel(true);
            rs = submit2.get();
        }
        System.out.println(rs);
        executorService.shutdown();

    }
}
