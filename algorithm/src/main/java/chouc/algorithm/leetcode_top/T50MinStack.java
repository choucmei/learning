package chouc.algorithm.leetcode_top;

import java.util.Stack;

public class T50MinStack {
    int min = Integer.MAX_VALUE;
    Stack<Long> stack;

    public T50MinStack() {
        stack = new Stack<>();
    }

    public void push(int val) {
        if (stack.empty()) {
            min = val;
            stack.push(0l);
        } else {
            stack.push(Long.valueOf(val) - min);
            min = Math.min(val, min);
        }
    }

    public void pop() {
        Long pop = stack.pop();
        if (pop >= 0) {
            // ans = pop + min;
        } else {
            // ans = min
            min = (int) (min - pop);
        }
    }

    public int top() {
        Long pop = stack.peek();
        if (pop >= 0) {
            return (int) (pop + min);
        } else {
            return min;
        }
    }

    public int getMin() {
        return min;
    }


    public static void main(String[] args) {

    }
}
