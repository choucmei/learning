package chouc.java.thread.comm01;

import java.util.Date;

/**
 * @author chouc
 * @version V1.0
 * @Title: MainThreadLocal02
 * @Package chouc.java.thread.comm01
 * @Description:
 * @date 1/17/20
 */
public class MainThreadLocal02 {
    public static ThreadLocalExt tl = new ThreadLocalExt();
    public static void main(String[] args) {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("       在Main线程中取值=" + tl.get());
                Thread.sleep(100);
            }
            Thread.sleep(5000);
            ThreadA a = new ThreadA();
            a.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static public class ThreadLocalExt extends ThreadLocal {
        @Override
        protected Object initialValue() {
            return new Date().getTime();
        }
    }

    static public class ThreadA extends Thread {

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println("在ThreadA线程中取值=" + MainThreadLocal02.tl.get());
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
