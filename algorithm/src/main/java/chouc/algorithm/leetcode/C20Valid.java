package chouc.algorithm.leetcode;

import java.util.Stack;

/**
 * @author chouc
 * @version V1.0
 * @Title: C20Valid
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/13
 */
public class C20Valid {
    public boolean isValid(String s) {
        if (s.length() % 2 != 0) {
            return false;
        }
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (!stack.isEmpty()){
                    char inC = stack.pop();
                    if (c == ')' && inC == '(') {
                        continue;
                    } else if (c == ']' && inC == '[') {
                        continue;
                    } else if (c == '}' && inC == '{') {
                        continue;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        if (stack.isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        C20Valid c=  new C20Valid();
        System.out.println(c.isValid("{[]}"));
    }
}
