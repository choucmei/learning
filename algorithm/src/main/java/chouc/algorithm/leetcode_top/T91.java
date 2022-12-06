package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.Utils;

public class T91 {
    public boolean checkValidString(String s) {
        int length = s.length();
        boolean dp[][] = new boolean[length][length];
        for (int i = 0; i < length; i++) {
            if (s.charAt(i) == '*') {
                dp[i][i] = true;
            }
        }
        for (int i = 1; i < length; i++) {
            char pre = s.charAt(i - 1), cur = s.charAt(i);
            dp[i - 1][i] = (pre == '(' || pre == '*') && (cur == ')' || cur == '*');
        }

        for (int i = 2; i < length; i++) {
            for (int j = i; j < length; j++) {
                char pre = s.charAt(j - i), cur = s.charAt(j);
                dp[j - i][j] = (pre == '(' || pre == '*') && (cur == ')' || cur == '*') && dp[j - i + 1][j - 1];
                for (int k = j - i + 1; k < j; k++) {
                    if (dp[j - i][j]) {
                        break;
                    }
                    dp[j - i][j] = (dp[j - i][k] && dp[k + 1][j]) || (dp[j - i][k - 1] && dp[k][j]);
                }
            }
        }
        Utils.printBooleanArray(dp);
        System.out.println(dp[0][length - 1]);
        return dp[0][length - 1];
    }

    public static void main(String[] args) {
        T91 t91 = new T91();
        t91.checkValidString("(*())");
    }
}
