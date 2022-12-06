package chouc.algorithm.leetcode_top;

import java.util.Stack;

public class T55 {
    public String decodeString(String s) {
        StringBuffer stringBuffer = new StringBuffer();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int count = 0;
            while (c >= '0' && c <= '9') {
                count = count * 10 + c - '0';
                i++;
                c = s.charAt(i);
            }
            int lc = 0, start = i;
            if (c == '[') {
                lc += 1;

                while (lc > 0) {
                    i++;
                    c = s.charAt(i);
                    if (c == '[') {
                        lc += 1;
                    } else if (c == ']') {
                        lc--;
                    }
                }
                String comStr = decodeString(s.substring(start + 1, i));
                for (int i1 = 0; i1 < count; i1++) {
                    stringBuffer.append(comStr);
                }
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        T55 t55 = new T55();
        System.out.println(t55.decodeString("3[a]2[bc]"));
        System.out.println(t55.decodeString("3[a2[c]]"));
        System.out.println(t55.decodeString("100[leetcode]"));

//        "3[a2[c]]"
    }
}



