package chouc.java.thread.sync;

import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: SyncMultiClass
 * @Package chouc.java.thread.sync
 * @Description:
 * @date 1/16/20
 */
public class SyncMultiClass {

    public static synchronized void println(){
        System.out.println("static synchronized println enter");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("static synchronized println exit");
    }


    public static void main(String[] args) {
        Data data01 = new Data01();
        Data data02 = new Data02();
        Data data03 = new Data03();
        Thread t1 = new ThreadClass(data01);
        Thread t2 = new ThreadClass(data02);
        Thread t3 = new ThreadClass(data03);

        t1.start();
        t2.start();
        t3.start();
    }

}

interface Data {
    void println();
}

class Data01 implements Data {
    @Override
    public void println() {
        synchronized (SyncMultiClass.class) {
            System.out.println("Data01 println enter");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Data01 println exit");
        }
    }
}

class Data02 implements Data {
    @Override
    public void println() {
        synchronized (SyncMultiClass.class) {
            System.out.println("Data02 println enter");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Data02 println exit");
        }
    }
}

class Data03 implements Data{
    @Override
    public void println() {
        SyncMultiClass.println();
    }
}

class ThreadClass extends Thread {
    Data data;

    public ThreadClass(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        data.println();
    }
}