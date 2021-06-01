package chouc.java.thread.pool;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author chouc
 * @version V1.0
 * @Title: PriorityPoolDemo
 * @Package chouc.java.thread.pool
 * @Description:
 * @date 2021/3/8
 */
public class PriorityPoolDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1,
                        0L, TimeUnit.HOURS,
                        new PriorityBlockingQueue<>(), Executors.defaultThreadFactory());
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Job1(i));
            executorService.execute(new Job2(i));
            executorService.execute(new Job3(i));
            Thread.sleep(500);
        }
        System.out.println("submited");
        executorService.awaitTermination(100, TimeUnit.MINUTES);
        System.out.println("over");
    }
}

abstract class AbstractJob implements Runnable, Comparable<AbstractJob> {
    int level = 1;
    protected Date date;
    protected Integer jobId;

    @Override
    public int compareTo(AbstractJob o) {
        return level - o.level;
    }
}

class Job1 extends AbstractJob {
    public Job1(int jobId) {
        this.level = 1;
        this.jobId = jobId;
        this.date = new Date();
        System.out.println("CCCreate"+" class:" + this.getClass().getName() + " level:" + level + " jobId:" + jobId + " date:" + date);
    }

    @Override
    public void run() {
        System.out.println("EEExcute"+" class:" + this.getClass().getName() + " level:" + level + " jobId:" + jobId + " date:" + date);
        try {
            Thread.sleep(1000 * 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Job2 extends AbstractJob {
    public Job2(int jobId) {
        this.level = 2;
        this.jobId = jobId;
        this.date = new Date();
        System.out.println("CCCreate"+" class:" + this.getClass().getName() + " level:" + level + " jobId:" + jobId + " date:" + date);
    }

    @Override
    public void run() {
        System.out.println("EEExcute"+" class:" + this.getClass().getName() + " level:" + level + " jobId:" + jobId + " date:" + date);
        try {
            Thread.sleep(1000 * 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Job3 extends AbstractJob {
    public Job3(int jobId) {
        this.level = 2;
        this.jobId = jobId;
        this.date = new Date();
        System.out.println("CCCreate"+" class:" + this.getClass().getName() + " level:" + level + " jobId:" + jobId + " date:" + date);
    }

    @Override
    public void run() {
        System.out.println("EEExcute"+" class:" + this.getClass().getName() + " level:" + level + " jobId:" + jobId + " date:" + date);
        try {
            Thread.sleep(1000 * 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}