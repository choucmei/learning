package chouc.java.thread.comm01;

/**
 * @author chouc
 * @version V1.0
 * @Title: MainJoin
 * @Package chouc.java.thread.comm01
 * @Description:
 * @date 1/17/20
 */
public class MainJoin {

    public static void main(String[] args) throws InterruptedException {

        MyThread threadTest = new MyThread();
        threadTest.start();

        //Thread.sleep(?);//因为不知道子线程要花的时间这里不知道填多少时间
        threadTest.join();
        System.out.println("我想当threadTest对象执行完毕后我再执行");
    }
    static public class MyThread extends Thread {

        @Override
        public void run() {
            System.out.println("我想先执行");
        }

    }
}
