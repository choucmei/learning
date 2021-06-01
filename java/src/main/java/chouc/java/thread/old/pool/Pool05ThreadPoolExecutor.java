package chouc.java.thread.old.pool;

import java.util.concurrent.*;

/**
 * @author chouc
 * @version V1.0
 * @Title: Pool05ThreadPoolExecutor
 * @Package chouc.java.thread.old.pool
 * @Description:
 * @date 7/8/20
 */
public class Pool05ThreadPoolExecutor {
    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(
                5,
                5,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
                );


        executorService.shutdown();
    }
}
