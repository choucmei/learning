package chouc.algorithm.leetcode_top;

import java.util.LinkedList;
import java.util.Queue;

public class T69 {
    Queue<Integer> core = new LinkedList<>(), out = new LinkedList<>(), tmp;

    public T69() {

    }

    public void push(int x) {
        core.add(x);
        while (!out.isEmpty()) {
            core.add(out.poll());
        }
        tmp = core;
        core = out;
        out = tmp;
    }

    public int pop() {
        return out.poll();
    }

    public int top() {
        return out.peek();
    }

    public boolean empty() {
        return out.isEmpty();
    }


    public static void main(String[] args) {
//        ["MyStack","push","push","top","pop","empty"]
//        [[],[1],[2],[],[],[]]
        T69 t69 = new T69();
        t69.push(1);
        t69.push(2);
        System.out.println(t69.top());
        System.out.println(t69.pop());
        System.out.println(t69.empty());
    }
}
