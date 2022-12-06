package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.Utils;

import java.util.Comparator;
import java.util.PriorityQueue;

public class T46 {
    public int[] maxSlidingWindow(int[] nums, int k) {
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[1] == o2[1] ? o2[0] - o1[0] : o2[1] - o1[1];
            }
        });
        int len = nums.length, ansIndex = 0;
        int[] ans = new int[len - k + 1];
        for (int i = 0; i < k; i++) {
            priorityQueue.add(new int[]{i, nums[i]});
        }
        ans[ansIndex++] = priorityQueue.peek()[1];
        for (int i = k; i < len; i++) {
            priorityQueue.add(new int[]{i, nums[i]});
            while (priorityQueue.peek()[0] <= i - k) {
                priorityQueue.poll();
            }
            ans[ansIndex++] = priorityQueue.peek()[1];
        }
        return ans;
    }

    public static void main(String[] args) {
        T46 t46 = new T46();
        int[] ints = t46.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3);
        int[] ints2 = t46.maxSlidingWindow(new int[]{9, 10, 9, -7, -4, -8, 2, -6}, 5);
        Utils.printArray(ints2);
    }

}
