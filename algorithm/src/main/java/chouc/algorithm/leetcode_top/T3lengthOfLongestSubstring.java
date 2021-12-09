package chouc.algorithm.leetcode_top;

import java.util.HashSet;
import java.util.Set;

public class T3lengthOfLongestSubstring {


//    public int lengthOfLongestSubstring(String s) {
//        Set<Character> set = new HashSet<>(s.length());
//        int maxLength = 0;
//        for (int i = 0; i < s.length(); i++) {
//            for (int j = i; j < s.length(); j++) {
//                char c = s.charAt(j);
//                if (set.contains(c)){
//                    break;
//                } else {
//                    set.add(c);
//                }
//            }
//            maxLength = Math.max(maxLength,set.size());
//            set.clear();
//        }
//        return maxLength;
//    }

    public int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>(s.length());
        int left = 0;
        int maxLength = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (set.contains(c)) {
                char removeC;
                while ((removeC = s.charAt(left)) != c) {
                    left++;
                    set.remove(removeC);
                }
                left++;
            } else {
                set.add(c);
            }
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }

    public static void main(String[] args) {
        T3lengthOfLongestSubstring t3lengthOfLongestSubstring = new T3lengthOfLongestSubstring();
        System.out.println(t3lengthOfLongestSubstring.lengthOfLongestSubstring("abcabcbb"));
    }
}
