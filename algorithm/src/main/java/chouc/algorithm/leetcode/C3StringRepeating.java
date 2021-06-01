package chouc.algorithm.leetcode;

import java.util.HashMap;

/**
 * @author chouc
 * @version V1.0
 * @Title: C3StringRepeating
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/10/17
 */
public class C3StringRepeating {
    public int lengthOfLongestSubstring(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        int maxLength = 0, index = -1, pre = 0;
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            if (map.containsKey(s.charAt(i))) {
                index = map.get(c);
                pre = Math.max(pre,index + 1);
            }
            map.put(c, i);
            maxLength = Math.max(maxLength,i + 1 - pre);
        }
        return maxLength;
    }

    public static void main(String[] args) {
        C3StringRepeating c = new C3StringRepeating();
        System.out.println(c.lengthOfLongestSubstring("aab"));
    }
}
