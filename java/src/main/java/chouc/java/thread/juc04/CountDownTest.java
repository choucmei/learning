package chouc.java.thread.juc04;

import java.util.concurrent.CountDownLatch;

/**
 * @author chouc
 * @version V1.0
 * @Title: CountDownTest
 * @Package chouc.java.thread.juc04
 * @Description:
 * @date 1/20/20
 */
public class CountDownTest {

    static class Task extends Thread {
        private CountDownLatch countDownLatch;

        public Task(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println( Thread.currentThread().getName() +" start work ");
        }
    }

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(3);
        Task task1 = new Task(countDownLatch);
        Task task2 = new Task(countDownLatch);
        Task task3 = new Task(countDownLatch);
        task1.setName("task1");
        task2.setName("task2");
        task3.setName("task3");
        task1.start();
        task2.start();
        task3.start();
        doingSomething(countDownLatch);
        doingSomething(countDownLatch);
        doingSomething(countDownLatch);

    }

    public static void doingSomething(CountDownLatch countDownLatch){
        System.out.println(" doingSomething ");
        countDownLatch.countDown();
    }


}