package chouc.java.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Pool01ExecutorDemo {
    public static void main(String[] args) {
        //单线程
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        //多线程
        int cpuNums = Runtime.getRuntime().availableProcessors();
        ExecutorService fixed = Executors.newFixedThreadPool(cpuNums);
        //多线程调度
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(8);
        //单线程调度
        ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }
}
