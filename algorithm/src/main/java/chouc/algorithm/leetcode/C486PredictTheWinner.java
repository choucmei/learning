package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C486PredictWinner
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/3/12
 */
public class C486PredictTheWinner {
    public boolean PredictTheWinner(int[] nums) {
        int length = nums.length;
        int[][] dp = new int[length][length];
        for (int i = 0; i < length; i++) {
            dp[i][i] = nums[i];
        }
        for (int i = 1; i < length; i++) {
//            for (int j = length - 2; j--) {
//                dp[i][j] = Math.max(dp[i-1][j],dp[i][j+1]);
//
//
//                dp[i][j] = Math.max(nums[i] - dp[i + 1][j], nums[j] - dp[i][j - 1]);
//
//
//            }
        }
        return false;

    }
}
