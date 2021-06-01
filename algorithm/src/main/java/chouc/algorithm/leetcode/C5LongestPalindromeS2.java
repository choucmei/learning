package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C5LongestRecycle
 * @Package chouc.java.algorithm.leetcode
 * @Description: 方法一：动态规划
 * @date 2020/11/30
 */
public class C5LongestPalindromeS2 {
    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }
        int length = s.length();
        int start = 0, end = 0;
        for (int i = 0; i < length; i++) {
            int length1 = expandAroundCenter(s, i, i);
            int length2 = expandAroundCenter(s, i, i + 1);
            int maxLength = Math.max(length1, length2);
            if (maxLength > end - start) {
                start = i - (maxLength - 1) / 2;
                end = i + maxLength / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    public static int expandAroundCenter(String str, int s, int e) {
        int length = str.length();
        while (s >= 0 && e < length && str.charAt(s) == str.charAt(e)) {
            s--;
            e++;
        }
        return (e - 1) - (s + 1) + 1;
    }

    public static void main(String[] args) {
        String a = "asfhlweihashajsdfoiwjliasdjfionoqefiowe";
//        String a = "cbbd";
        System.out.println(longestPalindrome(a));
    }


}
