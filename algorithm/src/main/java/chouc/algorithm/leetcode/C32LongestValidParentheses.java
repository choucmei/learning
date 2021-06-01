package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C32LongestValidParentheses
 * @Package chouc.java.algorithm.leetcode
 * @Description: 32. 最长有效括号
 * @date 2021/1/12
 */
public class C32LongestValidParentheses {
    public int longestValidParentheses(String s) {
        int l = s.length();
        int[] dp = new int[l];
        int maxLength = 0;
        for (int i = 1; i < l; i++) {
            if (s.charAt(i) == ')') {
                System.out.println("i:"+i+"   i - dp[i - 1] - 1: " + (i - dp[i - 1] - 1));
                if (i - dp[i - 1] - 1 > 0) {
                    System.out.println(s.charAt(i - dp[i - 1] - 1));
                }
                if (s.charAt(i - 1) == '(') {
                    System.out.println(s.charAt(i - 1) == '(');
                    dp[i] = i - 2 > 0 ? dp[i - 2] + 2 : 2;
                } else if (dp[i - 1] > 0 && i - dp[i - 1] - 1 >= 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
                    System.out.println("i:" + i);
                    if (i - dp[i - 1] - 2 > 0) {
                        dp[i] = dp[i - dp[i - 1] - 2] + dp[i - 1] + 2;
                    } else {
                        dp[i] = dp[i - 1] + 2;
                    }
                }
            }
            if (maxLength < dp[i]) {
                maxLength = dp[i];
            }
        }
        Utils.printArray(dp);
        return maxLength;
    }

    public static void main(String[] args) {
        C32LongestValidParentheses c = new C32LongestValidParentheses();
        System.out.println(c.longestValidParentheses("(()())"));
    }
}
