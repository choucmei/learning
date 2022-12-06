package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.Utils;

public class T26 {
    public int lengthOfLIS(int[] nums) {
        int length = nums.length, max = 0;
        int dp[] = new int[length];
        for (int i = 0; i < dp.length; i++) {
            dp[i] = 1;
        }
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[j]+1,dp[i]);
                    Utils.printArray(dp);
                }
            }
            max = Math.max(dp[i],max);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] t = new int[]{10,9,2,5,3,7,101,18,19};
        T26 t26 = new T26();
        System.out.println(t26.lengthOfLIS(t));
    }
}
