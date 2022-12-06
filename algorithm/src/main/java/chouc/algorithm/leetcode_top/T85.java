package chouc.algorithm.leetcode_top;

import java.util.Deque;
import java.util.LinkedList;

public class T85 {
    public String simplifyPath(String path) {
        String[] split = path.split("/");
        Deque<String> stack = new LinkedList<>();
        for (String s : split) {
            if (s.equals("") || s.equals(".")) {
                continue;
            } else if (s.equals("..")) {
                if (!stack.isEmpty()) {
                    stack.pollLast();
                }
            } else {
                stack.addLast(s);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append("/").append(stack.pollFirst());
        }
        if (sb.length() == 0){
            sb.append("/");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        T85 t85 = new T85();
        t85.simplifyPath("/a/b/c");
        t85.simplifyPath("/a/b/");
    }
}
