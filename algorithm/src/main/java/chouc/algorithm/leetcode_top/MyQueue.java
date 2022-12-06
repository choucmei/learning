package chouc.algorithm.leetcode_top;

import java.util.Stack;

public class MyQueue {
    Stack<Integer> stackIn;
    Stack<Integer> stackOut;

    public MyQueue() {
        stackIn = new Stack<>();
        stackOut = new Stack<>();
    }

    public void push(int x) {
        stackIn.push(x);
    }

    public int pop() {
        init();
        return stackOut.pop();
    }

    public int peek() {
        init();
        return stackOut.peek();
    }

    public void init() {
        if (stackOut.empty()) {
            while (!stackIn.empty()) {
                stackOut.add(stackIn.pop());
            }
        }
    }

    public boolean empty() {
        return stackOut.empty() && stackIn.empty();
    }
}
