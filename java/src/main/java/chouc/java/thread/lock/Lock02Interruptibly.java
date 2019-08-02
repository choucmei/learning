package chouc.java.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lock02Interruptibly {
    private Lock lock = new ReentrantLock();
    public void insert(Thread thread) throws InterruptedException {
//        lock.lock();
        lock.lockInterruptibly();//注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
        try {
            System.out.println(thread.getName()+"得到了锁");
            long startTime = System.currentTimeMillis();
            for(    ;     ;) {
                if(System.currentTimeMillis() - startTime >= 20000)
                    break;
                //插入数据
            }
        }
        finally {
            System.out.println(Thread.currentThread().getName()+"执行finally");
            lock.unlock();
            System.out.println(thread.getName()+"释放了锁");
        }
    }

    public static void main(String[] args) {
        Lock02Interruptibly lock02Interruptibly = new Lock02Interruptibly();
        MyThread thread0 = new MyThread(lock02Interruptibly);
        MyThread thread1 = new MyThread(lock02Interruptibly);
        thread0.start();
        thread1.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1.interrupt();
    }
}


class MyThread extends Thread {
    private Lock02Interruptibly lock02Interruptibly = null;
    public MyThread(Lock02Interruptibly lock02Interruptibly) {
        this.lock02Interruptibly = lock02Interruptibly;
    }
    @Override
    public void run() {

        try {
            lock02Interruptibly.insert(Thread.currentThread());
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName()+"被中断");
        }
    }
}
