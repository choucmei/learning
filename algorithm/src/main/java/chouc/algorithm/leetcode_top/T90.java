package chouc.algorithm.leetcode_top;

import java.util.Deque;
import java.util.LinkedList;

public class T90 {
    public String removeDuplicates(String s) {
        Deque<Character> stack = new LinkedList<>();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            if (!stack.isEmpty() && stack.peekLast() == s.charAt(i)) {
                stack.pollLast();
            } else {
                stack.addLast(s.charAt(i));
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pollFirst());
        }
        return sb.toString();
    }
}
