package chouc.java.thread.exec;

import java.util.concurrent.*;

/**
 * @author chouc
 * @version V1.0
 * @Title: ExecutorsInfo
 * @Package chouc.java.thread.exec
 * @Description:
 * @date 1/19/20
 */
public class ExecutorsInfo {

    void standardNew() {
        ExecutorService executorService =
                new ThreadPoolExecutor(1,1,
                        0L, TimeUnit.HOURS,
                        new ArrayBlockingQueue(10),Executors.defaultThreadFactory());
    }


    void newFixedThreadPool() {

        /**
         *    new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
         */

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    void newSingleThreadExecutor() {

        /**
         *          new FinalizableDelegatedExecutorService (new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));
         */

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    void newCachedThreadPool() {

        /**
         *       new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
         */
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    void newScheduledThreadPool() {
        /**
         *          new ScheduledThreadPoolExecutor(corePoolSize);
         */
        ExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


}
