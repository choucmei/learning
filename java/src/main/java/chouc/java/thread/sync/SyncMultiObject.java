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
public class SyncMultiObject {

    public static void main(String[] args) {
        Data data04 = new Data04();
        Data data05 = new Data05();
        Thread t1 = new ThreadClass(data04);
        Thread t2 = new ThreadClass(data05);

        t1.start();
        t2.start();
    }

}

class Data04 implements Data {
    @Override
    public void println() {
        synchronized (this) {
            System.out.println("Data04 println enter");
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Data04 println exit");
        }
    }
}

class Data05 implements Data {
    @Override
    public void println() {
        synchronized (this) {
            System.out.println("Data05 println enter");
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Data05 println exit");
        }
    }
}