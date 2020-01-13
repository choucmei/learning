package chouc.java.thread.model04;

import java.io.IOException;

/**
 * @author chouc
 * @version V1.0
 * @Title: SaverThread
 * @Package chouc.java.thread.model04
 * @Description:
 * @date 1/10/20
 */
//存储线程每个1s，会对数据进行一次保存，就像文本处理软件的“自动保存”一样。
public class SaverThread extends Thread {
    private Data data;
    public SaverThread(String name, Data data) {
        super(name);
        this.data = data;
    }
    public void run() {
        try {
            while (true) {
                data.save(); // 存储资料
                Thread.sleep(1000); // 休息约1秒
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}