package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C131Partition
 * @Package chouc.java.algorithm.leetcode
 * @Description: 分割回文串
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是 回文串 。返回 s 所有可能的分割方案。
 * 回文串 是正着读和反着读都一样的字符串。
 * 回溯
 * @date 2021/5/7
 */
public class C131Partition {
    public List<List<String>> partition(String str) {
        List<List<String>> result = new ArrayList<>();
        dfs(result, new ArrayList<>(), str, 0);
        return result;
    }

    public void dfs(List<List<String>> result, List<String> current, String str, int start) {
        if (start == str.length()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < str.length(); i++) {
            String sub = str.substring(start, i+1);
            boolean is = isPalindrome(sub, 0, sub.length() - 1);
            if (is) {
                current.add(sub);
                dfs(result, current, str, i+1);
            } else {
                continue;
            }
            current.remove(current.size() - 1);
        }
    }

    private boolean isPalindrome(String s, int start, int end) {
        if (start >= end) {
            return true;
        }
        return s.charAt(start) == s.charAt(end) && isPalindrome(s, start + 1, end - 1);
    }

    public static void main(String[] args) {
        C131Partition c = new C131Partition();
        List<List<String>> list = c.partition("aba");
        for (List<String> l:list){
            for (String s:l){
                System.out.print(s+"-");
            }
            System.out.println();
        }
    }
}
