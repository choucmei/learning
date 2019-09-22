package chouc.java.thread.notify;

public class NotifyExample {
    static Object lock1 = new Object();
    static Object lock2 = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(()-> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                lock1.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(()-> {
            try {
                lock1.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.notify();
        });
        Thread thread3 = new Thread(()-> {
            try {
                lock2.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        thread3.setName("thread3");
        thread2.setName("thread2");
        thread1.setName("thread1");

        thread3.start();
        thread2.start();
        thread1.start();


    }
}
