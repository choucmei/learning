package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.Utils;

import java.util.Arrays;

public class T51 {
    public int coinChange1(int[] coins, int amount) {
        int[][] dp = new int[coins.length + 1][amount + 1];
        int oLen = amount + 1, iLen = coins.length + 1;
        for (int i = 1; i < oLen; i++) {
            for (int j = 1; j < iLen; j++) {
                dp[j][i] = amount + 1;
            }
        }
        Arrays.sort(coins);
        for (int i = 1; i < oLen; i++) {
            for (int j = 1; j < iLen; j++) {
                if (i - coins[j - 1] >= 0) {
                    if (dp[j][i - coins[j - 1]] >= 0 || i - coins[j - 1] == 0) {
                        dp[j][i] = dp[j][i - coins[j - 1]] + 1;
                    }
                } else {
                    if (dp[j - 1][i] != 0) {
                        dp[j][i] = dp[j - 1][i];
                    }
                }
            }
        }
        Utils.printArrayInArray(dp);
        return dp[coins.length][amount] > amount ? -1 : dp[coins.length][amount];
    }


    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        int max = amount + 1;
        Arrays.fill(dp, max);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (coins[j] <= i) {
                    dp[i] = Math.min(dp[i - coins[j]] + 1, dp[i]);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }


    public static void main(String[] args) {
        T51 t51 = new T51();
        System.out.println(t51.coinChange(new int[]{1, 2, 5}, 11));
        System.out.println(t51.coinChange(new int[]{2}, 3));
        System.out.println(t51.coinChange(new int[]{1}, 0));
        System.out.println(t51.coinChange(new int[]{2}, 1));
        System.out.println(t51.coinChange(new int[]{1, 2, 5}, 11));
        System.out.println(t51.coinChange(new int[]{2, 5, 10, 1}, 27));
        System.out.println(t51.coinChange(new int[]{186, 419, 83, 408}, 6249));


        System.out.println("-----------------------");


        System.out.println(t51.coinChange1(new int[]{1, 2, 5}, 11));
        System.out.println(t51.coinChange1(new int[]{2}, 3));
        System.out.println(t51.coinChange1(new int[]{1}, 0));
        System.out.println(t51.coinChange1(new int[]{2}, 1));
        System.out.println(t51.coinChange1(new int[]{1, 2, 5}, 11));
        System.out.println(t51.coinChange1(new int[]{2, 5, 10, 1}, 27));
        System.out.println(t51.coinChange1(new int[]{186, 419, 83, 408}, 6249));
    }

}
