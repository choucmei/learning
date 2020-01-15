package chouc.java.thread.old.join;

public class ThreadJoin implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new ThreadJoin());
        Thread thread2 = new Thread(new ThreadJoin());
        Thread thread3 = new Thread(new ThreadJoin());
        thread1.setName("thread1");
        thread2.setName("thread2");
        thread3.setName("thread3");
        thread1.start();
        thread1.join();
        thread2.start();
        thread2.join();
        thread3.start();
    }


    @Override
    public void run() {
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
        System.out.println(Thread.currentThread().getName() + " end ");
    }
}
