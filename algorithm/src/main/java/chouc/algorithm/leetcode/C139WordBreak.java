package chouc.algorithm.leetcode;

import java.util.Arrays;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C139WordBreak
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/4/26
 */
public class C139WordBreak {
    public boolean wordBreak(String s, List<String> wordDict) {
        int stringLength = s.length(),index;
        boolean[] dp = new boolean[stringLength];
        boolean workEquals;
        for (int i = 0; i < stringLength; i++) {
            for (String word : wordDict) {
                int wordLength = word.length();
                if (i + wordLength-1 < stringLength) {
                    workEquals = s.substring(i, i + wordLength).equals(word);
                    index = i + wordLength - 1;
                    if (i == 0) {
                        dp[index] = workEquals || dp[index];
                    } else {
                        dp[index] = dp[index] || (workEquals && dp[i - 1]);
                    }
                }
            }
        }
        return dp[stringLength - 1];
    }

    public static void main(String[] args) {
        String a = "applepenapple";
        List<String> list = Arrays.asList("apple", "pen");
        C139WordBreak c = new C139WordBreak();
        System.out.println(c.wordBreak(a, list));
    }
}
