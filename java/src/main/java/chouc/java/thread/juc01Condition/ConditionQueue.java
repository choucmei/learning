package chouc.java.thread.juc01Condition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chouc
 * @version V1.0
 * @Title: ConditionQueue
 * @Package chouc.java.thread.juc01Condition
 * @Description:
 * @date 7/2/20
 */
public class ConditionQueue {

    private ReentrantLock lock = new ReentrantLock();
    private LinkedList<String> queue = new LinkedList<String>();
    Condition isNotFull = lock.newCondition();
    Condition isNotEmpty = lock.newCondition();
    private final static int MAX_SIZE = 10;

    public String take() throws InterruptedException {
        String value = null;
        lock.lock();
        try {
            if (queue.size() == 0){
                System.out.println("empty wait.....");
                isNotEmpty.wait();
            }
            System.out.println(" get ");
            value = queue.remove(0);
            isNotFull.signal();
        } finally {
            lock.unlock();
        }
        return value;
    }
    public void put(String value) throws InterruptedException {
        lock.lock();
        try {
            if (queue.size() >= MAX_SIZE){
                System.out.println("full wait.....");
                isNotFull.await();
            }
            System.out.println(" put ");
            queue.push(value);
            isNotEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ConditionQueue conditionQueue = new ConditionQueue();
        Thread producer = new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                        conditionQueue.put(".....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread comsumer = new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(conditionQueue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        producer.start();
        comsumer.start();
    }
}
