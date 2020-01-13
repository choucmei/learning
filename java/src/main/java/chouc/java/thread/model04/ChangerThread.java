package chouc.java.thread.model04;

import java.io.IOException;
import java.util.Random;

/**
 * @author chouc
 * @version V1.0
 * @Title: ChangerThread
 * @Package chouc.java.thread.model04
 * @Description:
 * @date 1/10/20
 */
//修改线程模仿“一边修改文章，一边保存”
public class ChangerThread extends Thread {
    private Data data;
    private Random random = new Random();
    public ChangerThread(String name, Data data) {
        super(name);
        this.data = data;
    }
    public void run() {
        try {
            for (int i = 0; true; i++) {
                data.change("No." + i);
                Thread.sleep(random.nextInt(1000));
                data.save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}