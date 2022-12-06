package chouc.algorithm.leetcode_top;

import java.util.ArrayList;
import java.util.List;

// 输出：["((()))","(()())","(())()","()(())","()()()"]
public class T42 {
    List<String> strings;

    public List<String> generateParenthesis(int n) {
        strings = new ArrayList<>();
        traceback(new StringBuilder(), n, n);
        return strings;
    }

    private void traceback(StringBuilder sb, int s, int e) {
        if (s == 0 && e == 0) {
            strings.add(sb.toString());
            return;
        }
        if (s > 0) {
            sb.append('(');
            traceback(sb, s - 1, e);
            sb.deleteCharAt(sb.length() - 1);
        }
        if (s < e) {
            sb.append(')');
            traceback(sb, s, e - 1);
            sb.deleteCharAt(sb.length() - 1);
        }
    }


    public static void main(String[] args) {
        T42 t42 = new T42();
        System.out.println(t42.generateParenthesis(3));
    }
}
