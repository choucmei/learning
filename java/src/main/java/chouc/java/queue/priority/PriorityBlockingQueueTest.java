package chouc.java.queue.priority;

import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: PriorityBlockingQueueTest
 * @Package chouc.java.queue
 * @Description:
 * @date 2021/3/8
 */
public class PriorityBlockingQueueTest {

    public static void main(String[] args) throws InterruptedException {
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Integer v = random.nextInt(20);
            System.out.println("pre:" + v);
            priorityBlockingQueue.put(v);
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(priorityBlockingQueue.poll(10, TimeUnit.SECONDS));
        }

    }
}
