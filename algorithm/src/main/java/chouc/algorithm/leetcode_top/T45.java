package chouc.algorithm.leetcode_top;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class T45 {
    List result;
    String[] segments = new String[4];

    public List<String> restoreIpAddresses(String s) {
        result = new ArrayList();
        dfs(s, 0, 0);
        return result;
    }

    public void dfs(String s, int seg, int chatIndex) {
        if (chatIndex >= s.length() || seg >= 4) {
            if (seg == 4 && chatIndex == s.length()) {
                result.add(String.join(".", segments));
            }
            return;
        }
        int curSeg = s.charAt(chatIndex) - '0';
        segments[seg] = String.valueOf(curSeg);
        dfs(s, seg + 1, chatIndex + 1);
        if (curSeg == 0) {
            return;
        }
        chatIndex++;

        for (; chatIndex < s.length(); chatIndex++) {
            curSeg = curSeg * 10 + s.charAt(chatIndex) - '0';
            if (curSeg <= 255) {
                segments[seg] = String.valueOf(curSeg);
                dfs(s, seg + 1, chatIndex + 1);
            } else {
                break;
            }
        }
    }

    public static void main(String[] args) {
        T45 t45 = new T45();
        System.out.println(String.valueOf(0));
        System.out.println(String.valueOf(1));
//        System.out.println(t45.restoreIpAddresses("25525511135"));
//        System.out.println(t45.restoreIpAddresses("0000"));
        System.out.println(t45.restoreIpAddresses("1231231231234"));
    }
}
