package chouc.algorithm.leetcode_top;

public class T43 {
    public int longestCommonSubsequence(String text1, String text2) {
        int len1 = text1.length(), len2 = text2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 1; i <=len1; i++) {
            char c = text1.charAt(i - 1);
            for (int j = 1; j <= len2; j++) {
                if (c == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }
        return dp[len1][len2];
    }


    public static void main(String[] args) {
        T43 t43 = new T43();


//        System.out.println(t43.longestCommonSubsequence("bsbininm", "jmjkbkjkv"));
        System.out.println(t43.longestCommonSubsequence("abcde", "ace"));
        System.out.println(t43.longestCommonSubsequence("ace", "abcde"));

    }
}
