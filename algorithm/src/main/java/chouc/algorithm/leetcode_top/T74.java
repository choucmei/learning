package chouc.algorithm.leetcode_top;

public class T74 {
    public int numDecodings(String s) {
        int len = s.length();
        int[] dp = new int[len + 1];
        dp[0] = 1;
        for (int i = 1; i <= len; i++) {
            if (s.charAt(i - 1) != '0') {
                dp[i] = dp[i - 1];
            }
            if (i - 2 >= 0 && s.charAt(i - 2) > '0' && (s.charAt(i - 2) - '0') * 10 + (s.charAt(i - 1) - '0') <= 26) {
                dp[i] += dp[i - 2];
            }
        }
        return dp[len];
    }

    public static void main(String[] args) {
        T74 t74 = new T74();
        System.out.println(t74.numDecodings("2101"));
        System.out.println(t74.numDecodings("12"));
        System.out.println(t74.numDecodings("226"));
    }
}
