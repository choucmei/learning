package chouc.java.thread.vol01;

/**
 * @author chouc
 * @version V1.0
 * @Title: RunThread
 * @Package chouc.java.thread.vol01
 * @Description:
 * @date 1/17/20
 */
public class RunThread extends Thread {
    private boolean isRunning = true;
    int m;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        System.out.println("进入run了");
        while (isRunning == true) {
            int a = 2;
            int b = 3;
            int c = a + b;
            m = c;
//            System.out.println(m); 加不加 以这一行局别很大
        }
        System.out.println(m);
        System.out.println("线程被停止了！");
    }
}
