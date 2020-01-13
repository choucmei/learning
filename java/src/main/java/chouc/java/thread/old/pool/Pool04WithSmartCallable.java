package chouc.java.thread.old.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Pool04WithSmartCallable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Future<String>> list = new ArrayList<Future<String>>();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        for (int i = 0;i < 10;i++){
            //01 非调度
//            Future<String> future = scheduledExecutorService.submit(new Pool04MyCallable("****"+i+"****"));

            //02 调度
            Random random = new Random();
            Future<String> future = scheduledExecutorService.schedule(new Pool04MyCallable("****"+i+"****"),random.nextInt(10), TimeUnit.SECONDS);
            list.add(future);
        }
        for(Future<String> tem : list){
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            boolean done = tem.isDone();
            System.out.println(done?"已完成":"未完成");  //从结果的打印顺序可以看到，即使未完成，也会阻塞等待
            System.out.println("线程返回future结果： " + tem.get());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        scheduledExecutorService.shutdown();
    }


}
