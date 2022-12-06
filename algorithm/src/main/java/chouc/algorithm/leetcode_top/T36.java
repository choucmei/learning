package chouc.algorithm.leetcode_top;

public class T36 {
    public int minDistance(String word1, String word2) {
        int l1 = word1.length(), l2 = word2.length();
        int[][] dp = new int[l1 + 1][l2 + 1];
        for (int i = 0; i <= l1; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i <= l2; i++) {
            dp[0][i] = i;
        }
        for (int i = 0; i < l1; i++) {
            for (int j = 0; j < l2; j++) {
                if (word1.charAt(i) == word2.charAt(j)) {
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    dp[i + 1][j + 1] = Math.min(Math.min(dp[i][j + 1], dp[i + 1][j]), dp[i][j]) + 1;
                }
            }
        }
//        Utils.printArrayInArray(dp);
        return dp[l1][l2];
    }

    public static void main(String[] args) {
        T36 t36 = new T36();
        System.out.println(t36.minDistance("horse", "ros"));
        System.out.println(t36.minDistance("intention", "execution"));
        System.out.println(t36.minDistance("", "a"));
    }

}
