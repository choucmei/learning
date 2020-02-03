package chouc.java.thread.juc06Semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: SemaphoreTest
 * @Package chouc.java.thread.juc06
 * @Description:
 * @date 1/21/20
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        int parallelism = 5;
        SomePool somePool = new SomePool(5);
        for (int i = 0; i <= 5; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Random random = new Random();
                    for (int j = 0; j < 10; j++) {
                        System.out.println(Thread.currentThread().getName() + " start ");
                        String c = somePool.get();
                        System.out.println(Thread.currentThread().getName() + " got :" + c);
                        try {
                            TimeUnit.MILLISECONDS.sleep(random.nextInt(10));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        somePool.put(c);
                        System.out.println(Thread.currentThread().getName() + " put :" + c);
                    }
                }
            };
            thread.setName("t" + i);
            thread.start();
        }

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        somePool.print();
    }
}

class SomePool {
    int size;
    Semaphore semaphore;
    List<String> list;

    public SomePool(int size) {
        this.size = size;
        this.semaphore = new Semaphore(this.size);
        this.list = new ArrayList<>(this.size);
        for (int i = 0; i < this.size; i++) {
            list.add(" c " + i);
        }
    }

    public String get() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return g();
    }

    private synchronized String g() {
        String c = list.get(list.size() - 1);
        list.remove(c);
        return c;
    }

    public void put(String c) {
        semaphore.release();
        p(c);
    }

    private synchronized void p(String c) {
        list.add(c);
    }

    public void print() {
        System.out.println(String.join(",", list));
    }
}
