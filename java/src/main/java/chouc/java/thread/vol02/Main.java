package chouc.java.thread.vol02;

/**
 * @author chouc
 * @version V1.0
 * @Title: Main
 * @Package chouc.java.thread.vol02
 * @Description:
 * @date 1/17/20
 */
public class Main {
    public static void main(String[] args) {
        MyThread[] mythreadArray = new MyThread[2];
        for (int i = 0; i < 2; i++) {
            mythreadArray[i] = new MyThread();
        }

        for (int i = 0; i < 2; i++) {
            mythreadArray[i].start();
        }
    }
}
