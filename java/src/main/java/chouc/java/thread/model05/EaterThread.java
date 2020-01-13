package chouc.java.thread.model05;

import java.util.Random;

/**
 * @author chouc
 * @version V1.0
 * @Title: EaterThread
 * @Package chouc.java.thread.model05
 * @Description:
 * @date 1/10/20
 */
public class EaterThread extends Thread {
    private final Random random;
    private final Table table;
    public EaterThread(String name, Table table, long seed) {
        super(name);
        this.table = table;
        this.random = new Random(seed);
    }
    public void run() {
        try {
            while (true) {
                String cake = table.take();
                Thread.sleep(random.nextInt(1000));
            }
        } catch (InterruptedException e) {
        }
    }
}