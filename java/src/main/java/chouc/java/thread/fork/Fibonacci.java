package chouc.java.thread.fork;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author chouc
 * @version V1.0
 * @Title: Fibonacci
 * @Package chouc.java.thread.fork
 * @Description:
 * @date 7/8/20
 */
public class Fibonacci extends RecursiveTask<Integer> {

    int n;

    public Fibonacci(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }
        System.out.println("n = "+n);
        Fibonacci fibonacci1 = new Fibonacci(n - 1);
        fibonacci1.fork();
        Fibonacci fibonacci2 = new Fibonacci(n - 2);
        fibonacci2.fork();
        int fisrt = fibonacci1.join();
        int second = fibonacci2.join();
        System.out.println("first:"+fisrt+" second:"+second);
        int result =fisrt + second;
        System.out.println(result);
        return result;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> task = forkJoinPool.submit(new Fibonacci(10));
        System.out.println(task.get());
    }
}
