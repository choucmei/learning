package chouc.java.thread.old.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Lock03ReadWriteLock {
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    public void readOperate() throws InterruptedException {
        reentrantReadWriteLock.readLock().lock();
        System.out.println(Thread.currentThread().getName() + "... read start");
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 5000) {
            TimeUnit.MILLISECONDS.sleep(10);
            System.out.println(Thread.currentThread().getName() + "... read");
        }
        reentrantReadWriteLock.readLock().unlock();
        System.out.println(Thread.currentThread().getName()+"... read over");
    }

    public void writeOperate() throws InterruptedException {
        reentrantReadWriteLock.writeLock().lock();
        System.out.println(Thread.currentThread().getName()+"*** write start");
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 5000) {
            TimeUnit.MILLISECONDS.sleep(10);
            System.out.println(Thread.currentThread().getName() + "*** write");
        }
        reentrantReadWriteLock.writeLock().unlock();
        System.out.println(Thread.currentThread().getName()+"*** write over");
    }

    public static void main(String[] args) {
        final Lock03ReadWriteLock lock03ReadWriteLock = new Lock03ReadWriteLock();
        new Thread(){
            @Override
            public void run() {
                try {
                    lock03ReadWriteLock.readOperate();
                    lock03ReadWriteLock.writeOperate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                try {
                    lock03ReadWriteLock.readOperate();
                    lock03ReadWriteLock.writeOperate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }
}
