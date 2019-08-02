package chouc.java.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Lock03ReadWriteLock {
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    public void readOperate(){
        reentrantReadWriteLock.readLock().lock();
        System.out.println(Thread.currentThread().getName() + "... read start");
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 1) {
            System.out.println(Thread.currentThread().getName() + "... read");
        }
        reentrantReadWriteLock.readLock().unlock();
        System.out.println(Thread.currentThread().getName()+"... read over");
    }

    public void writeOperate(){
        reentrantReadWriteLock.writeLock().lock();
        System.out.println(Thread.currentThread().getName()+"*** write start");
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 1) {
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
                lock03ReadWriteLock.readOperate();
                lock03ReadWriteLock.writeOperate();
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                lock03ReadWriteLock.readOperate();
                lock03ReadWriteLock.writeOperate();
            }
        }.start();

    }
}
