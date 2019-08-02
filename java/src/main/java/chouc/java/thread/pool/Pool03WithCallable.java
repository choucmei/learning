package chouc.java.thread.pool;

import java.util.concurrent.*;

public class Pool03WithCallable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 5 ;i++){
            Future<String> stringFuture = executorService.submit(new Callable<String>(){

                public String call() throws Exception {
                    Thread.sleep(5000);
                    return "b--"+Thread.currentThread().getName();
                }
            });
            //从Future中get结果，这个方法是会被阻塞的，一直要等到线程任务返回结果
            System.out.println(stringFuture.get());
        }
        executorService.shutdown();
    }
}
