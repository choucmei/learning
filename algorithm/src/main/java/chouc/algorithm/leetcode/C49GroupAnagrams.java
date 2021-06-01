package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chouc
 * @version V1.0
 * @Title: C49GroupAnagrams
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/22
 */
public class C49GroupAnagrams {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            int[] count = new int[26];
            for (char c : str.toCharArray()) {
                count[c - 'a']++;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < count.length; i++) {
                for (; count[i] > 0; count[i]--) {
                    sb.append((char) (i + 'a'));
                }
            }
            map.compute(sb.toString(), (k, v) -> {
                if (v == null) {
                    v = new ArrayList<>();
                }
                v.add(str);
                return v;
            });
        }
        return new ArrayList<>(map.values());
    }

    public static void main(String[] args) {
        String[] str = {"eat", "tea", "tan", "ate", "nat", "bat"};
        C49GroupAnagrams c = new C49GroupAnagrams();
        Utils.printListInList(c.groupAnagrams(str));
    }
}
