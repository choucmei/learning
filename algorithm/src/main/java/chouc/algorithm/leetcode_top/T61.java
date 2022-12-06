package chouc.algorithm.leetcode_top;

public class T61 {
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length <= 1) return 0;
        int[][][] dp = new int[prices.length][2][3];
        int MIN_VALUE = Integer.MIN_VALUE / 2;//因为最小值再减去1就是最大值Integer.MIN_VALUE-1=Integer.MAX_VALUE
        // first day
        dp[0][0][0] = 0;
        dp[0][0][1] = dp[0][0][2] = MIN_VALUE;
        dp[0][1][1] = dp[0][1][2] = MIN_VALUE;
        dp[0][1][0] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][0][0] = 0;
            dp[i][0][1] = Math.max(dp[i - 1][1][0] + prices[i], dp[i - 1][0][1]);
            dp[i][0][2] = Math.max(dp[i - 1][1][1] + prices[i], dp[i - 1][0][2]);
            dp[i][1][0] = Math.max(dp[i - 1][0][0] - prices[i], dp[i - 1][1][0]);
            dp[i][1][1] = Math.max(dp[i - 1][0][1] - prices[i], dp[i - 1][1][1]);
            dp[i][1][2] = MIN_VALUE;
        }
        return Math.max(0, Math.max(dp[prices.length - 1][0][1], dp[prices.length - 1][0][2]));
    }

    public static void main(String[] args) {
        T61 t61 = new T61();
        System.out.println(t61.maxProfit(new int[]{3, 3, 5, 0, 0, 3, 1, 4}));
    }
}
