package chouc.java.thread.sync;

/**
 * @author chouc
 * @version V1.0
 * @Title: SyncMethod
 * @Package chouc.java.thread.sync
 * @Description:
 * @date 1/16/20
 */
public class SyncMethod extends Thread {
    private int num = 10;//共享数据

    @Override
    public void run() {
        try {
            this.fun();//调用同步方法
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void fun() throws InterruptedException {
        //使用睡眠来模拟一下复杂但是对数据处理没有关系的部分，睡眠三秒
        Thread.sleep(3000);
        System.out.println("修改前的num为" + num);
        num--;
        System.out.println("修改后的num为" + num);
        System.out.println("*************");
    }

    public static void main(String[] args) {

        SyncMethod synFun = new SyncMethod();
        Thread t1 = new Thread(synFun);
        Thread t2 = new Thread(synFun);
        t1.start();
        t2.start();

    }
}
