package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chouc
 * @version V1.0
 * @Title: C17PhoneCombine
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/10
 */
public class C17PhoneCombine {
    Map<Character, String> phoneMap = new HashMap<Character, String>() {{
        put('2', "abc");
        put('3', "def");
        put('4', "ghi");
        put('5', "jkl");
        put('6', "mno");
        put('7', "pqrs");
        put('8', "tuv");
        put('9', "wxyz");
    }};

    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.isEmpty()) {
            return result;
        }
        List<String> t = new ArrayList<>();
        for (char k : digits.toCharArray()) {
            t.add(phoneMap.get(k));
        }
        strace(new StringBuilder(), result, t, 0);
        return result;
    }


    private void strace(StringBuilder sb, List<String> result, List<String> target, int index) {
        if (index == target.size()){
            result.add(sb.toString());
            return;
        }
        for (char c : target.get(index).toCharArray()) {
            sb.append(c);
            strace(sb, result, target, index + 1);
            sb.deleteCharAt(sb.length() - 1);
        }
    }


    public static void main(String[] args) {
        C17PhoneCombine c = new C17PhoneCombine();
        System.out.println(c.letterCombinations("23"));
    }


}
