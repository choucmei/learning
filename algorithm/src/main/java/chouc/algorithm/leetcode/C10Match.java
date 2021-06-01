package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C10Match
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/5
 */
public class C10Match {
    public static boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();

        boolean[][] f = new boolean[m + 1][n + 1];
        f[0][0] = true;
        for (int i = 0; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (p.charAt(j - 1) == '*') {
                    System.out.println("i=" + i + ",j-2=" + (j - 2) + ",j-1=" + (j - 1) + "#######" + f[i][j - 2]);
                    f[i][j] = f[i][j - 2];
                    if (matches(s, p, i, j - 1)) {
                        System.out.println("i - 1=" + (i - 1) + "&&&&&&" + f[i - 1][j]);
                        f[i][j] = f[i][j] || f[i - 1][j];
                    }
                } else {
                    if (matches(s, p, i, j)) {
                        f[i][j] = f[i - 1][j - 1];
                    }
                }
                System.out.println("i=" + i + ",j=" + j + "********" + f[i][j]);
            }
        }

        return f[m][n];
    }

    public static boolean matches(String s, String p, int i, int j) {
        if (i == 0) {
            return false;
        }
        if (p.charAt(j - 1) == '.') {
            return true;
        }
        return s.charAt(i - 1) == p.charAt(j - 1);
    }


    public static boolean isMatch2(String s, String p) {
        int l1 = s.length();
        int l2 = p.length();
        boolean[][] result = new boolean[l1 + 1][l2 + 1];
        result[0][0]=true;
        for (int i = 0; i <= l1; i++) {
            for (int j = 1; j <= l2; j++) {
                if (p.charAt(j - 1) == '*') {
                    result[i][j] = result[i][j - 2];
                    if (maches2(s, p, i, j - 1)) {
                        result[i][j] = result[i][j] || result[i-1][j];
                    }
                } else {
                    if (maches2(s, p, i, j)) {
                        result[i][j] = result[i - 1][j - 1];
                    }
                }
            }
        }
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j] + ",");
            }
            System.out.println("");
        }
        return result[l1][l2];
    }

    public static boolean maches2(String s1, String s2, int i, int j) {
        if (i == 0) {
            return false;
        }
        if (s2.charAt(j - 1) == '.') {
            return true;
        }
        return s1.charAt(i - 1) == s2.charAt(j - 1);
    }

    public boolean isMatch1(String s, String p) {
        if (s == null || p == null || (!p.isEmpty() && p.charAt(0) == '*'))
            return false;
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int i = 0; i <= m; i++)
            for (int j = 1; j <= n; j++)
                if (p.charAt(j - 1) == '*')
                    dp[i][j] = dp[i][j - 2] || (i > 0 && (s.charAt(i - 1) == p.charAt(j - 2) || p.charAt(j - 2) == '.') && dp[i - 1][j]);
                else
                    dp[i][j] = i > 0 && dp[i - 1][j - 1] && (s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.');
        return dp[m][n];
    }



    public static void main(String[] args) {
        System.out.println(isMatch2("aabbbbccd", "aab*ccd"));
    }

}
