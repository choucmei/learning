package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C5LongestRecycle
 * @Package chouc.java.algorithm.leetcode
 * @Description: 方法一：动态规划
 * @date 2020/11/30
 */
public class C5LongestPalindrome {
    public static String longestPalindrome(String s) {
        int length = s.length();
        boolean[][] dp = new boolean[length][length];
        String res = "";
        for (int l = 0; l < length; l++) {
            for (int i = 0; i + l < length; i++) {
                int j = i + l;
                if (l == 0) {
                    dp[i][j] = true;
                } else if (l == 1) {
                    dp[i][j] = s.charAt(i) == s.charAt(j);
                } else {
                    dp[i][j] = s.charAt(i) == s.charAt(j) && dp[i+1][j-1];
                }
                if (dp[i][j] && (j - i + 1) > res.length()) {
                    res = s.substring(i, j + 1);
                    System.out.println(res);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String a = "aaaa";
        System.out.println(longestPalindrome(a));
    }


}
