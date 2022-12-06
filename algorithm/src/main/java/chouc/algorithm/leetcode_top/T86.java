package chouc.algorithm.leetcode_top;

import java.util.Deque;
import java.util.LinkedList;

public class T86 {
    String addStrings(String num1, String num2) {
        int len1 = num1.length(), len2 = num2.length(), s1 = len1 - 1, s2 = len2 - 1, add = 0;
        Deque<Integer> deque = new LinkedList<>();
        while (s1 >= 0 || s2 >= 0 || add > 0) {
            int v1 = s1 >= 0 ? Character.getNumericValue(num1.charAt(s1)) : 0;
            int v2 = s2 >= 0 ? Character.getNumericValue(num1.charAt(s2)) : 0;
            int sum = v1 + v2 + add;
            add = sum / 36;
            deque.addLast(sum % 36);
            s1--;
            s2--;
        }
        StringBuilder stringBuilder = new StringBuilder();
        while (!deque.isEmpty()) {
            stringBuilder.append(deque.pollLast());
        }
        return stringBuilder.toString();
    }

    int getValue(char a) {
        if (a > 9) {
            return Character.getNumericValue(a);
        } else {
            return a - '0';
        }
    }

    public static void main(String[] args) {
        System.out.println('A' - '0');
        T86 t86 = new T86();
        t86.addStrings("1b", "2x");
    }
}
