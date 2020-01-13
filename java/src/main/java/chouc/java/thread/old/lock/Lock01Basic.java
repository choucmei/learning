package chouc.java.thread.old.lock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lock01Basic {


    static Lock lock = new ReentrantLock();


    public static void main(String[] args) {
        new Thread(){
            @Override
            public void run() {
                lock.lock();
                System.out.println(" thread 0 gain lock ,then it sleeping");
                Random random = new Random();
                try {
                    Thread.sleep(random.nextInt(10)*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(" thread 0 release lock");
                lock.unlock();
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                lock.lock();
                System.out.println(" thread 1 gain lock ,then it sleeping");
                Random random = new Random();
                try {
                    Thread.sleep(random.nextInt(10)*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(" thread 1 release lock");
                lock.unlock();
            }
        }.start();
    }
    
}
