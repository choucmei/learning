package chouc.java.thread.juc01Condition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chouc
 * @version V1.0
 * @Title: ConditionTest
 * @Package chouc.java.thread
 * @Description:
 * @date 1/14/20
 */
public class ConditionTest {

    static class Data {
        final Lock lock = new ReentrantLock();
        Condition hasValue = lock.newCondition();

        String value;

        public String getValue() throws InterruptedException {
            lock.lock();
            try {
                if (value == null) {
                    hasValue.await();
                }
            } finally {
                lock.unlock();
            }
            return value;
        }

        public void setValue(String value) {
            lock.lock();
            try {
                this.value = value;
                hasValue.signal();
            } finally {
                lock.unlock();
            }
        }
    }


    static class CallableThread extends Thread {
        private Data data;

        public CallableThread(Data data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            data.setValue(" hhhh  ");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Data data = new Data();
        Thread thread = new CallableThread(data);
        thread.start();
        System.out.println(data.getValue());
    }
}
