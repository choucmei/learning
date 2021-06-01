package chouc.java.thread.fork;

import java.util.concurrent.*;

/**
 * @author chouc
 * @version V1.0
 * @Title: ArraySum
 * @Package chouc.java.thread.fork
 * @Description:
 * @date 7/9/20
 */
public class ArraySumTask extends RecursiveTask<Long> {

    private int[] array;
    private int startIndex;
    private int endIndex;

    public ArraySumTask(int[] array, int startIndex, int endIndex) {
        this.array = array;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    protected Long compute() {
        long result = 0L;
        if (endIndex - startIndex < 100) {
            for (int i = startIndex; i <= endIndex; i++) {
                result+=array[i];
            }
            return result;
        }
        int middleIndex = startIndex + (endIndex-startIndex)/2;
        ArraySumTask a1 = new ArraySumTask(array,startIndex,middleIndex);
        ArraySumTask a2 = new ArraySumTask(array,middleIndex + 1,endIndex);
        a1.fork();
        a2.fork();
        result = a1.join() + a2.join();
        System.out.println(result);
        return result;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> result = pool.submit(new ArraySumTask(new int[1000],0,999));
        System.out.println(result.get());
        if (result.isCompletedAbnormally()){
            System.out.println(result.getException());
        }

    }

}
