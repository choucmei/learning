package chouc.java.thread.pool;

import java.util.Random;
import java.util.concurrent.Callable;

public class Pool04MyCallable implements Callable<String> {
    private String name;

    public Pool04MyCallable(String name) {
        this.name = name;
    }

    public String call() throws Exception {
        String nameThread = Thread.currentThread().getName();
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(nameThread+" 启动时间：" + currentTimeMillis/1000 + name);

        Random random = new Random();
        int rint = random.nextInt(3);

        try {
            Thread.sleep(rint*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println(name + " is working..."+name);

        return name+"";
    }
}
