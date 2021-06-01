package chouc.algorithm.leetcode_hot;

import java.util.HashSet;

/**
 * @author chouc
 * @version V1.0
 * @Title: H3LongestString
 * @Package chouc.java.algorithm.leetcode_hot
 * @Description:
 * @date 2021/4/12
 */
public class H3LongestString {
    public int lengthOfLongestSubstring(String str) {
        HashSet<Character> set = new HashSet<>();
        int windowEnd = -1, maxLength = 0;
        for (int windowStart = 0; windowStart < str.length(); windowStart++) {
            System.out.println("1windowStart:"+windowStart+" windowEnd:"+windowEnd);
            if (windowStart != 0) {
                set.remove(str.charAt(windowStart - 1));
            }
            System.out.println("2windowStart:"+windowStart+" windowEnd:"+windowEnd);
            while (windowEnd + 1 < str.length() && !set.contains(str.charAt(windowEnd + 1))) {
                set.add(str.charAt(windowEnd + 1));
                windowEnd++;
            }
            System.out.println("3windowStart:"+windowStart+" windowEnd:"+windowEnd);
            maxLength = Math.max(maxLength, windowEnd - windowStart + 1);
        }
        return maxLength;
    }

    public static void main(String[] args) {
        H3LongestString c = new H3LongestString();
        System.out.println(c.lengthOfLongestSubstring("aab"));
    }
}
