package chouc.algorithm.leetcode_top;

import java.util.Arrays;

public class T54 {
    public int longestConsecutive(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        Arrays.sort(nums);
        int max = 1, pre = 0, count = 1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == nums[pre]) {
                continue;
            }
            if (nums[i] == nums[pre] + 1) {
                count++;
                max = Math.max(count, max);
            } else {
                count = 1;
            }
            pre = i;
        }
        return max;
    }

    public static void main(String[] args) {
        int[] t = new int[]{100, 4, 200, 1, 3, 2};
        int[] t2 = new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1};
        T54 t54 = new T54();
        System.out.println(t54.longestConsecutive(t));
        System.out.println(t54.longestConsecutive(t2));

    }
}
