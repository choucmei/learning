package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

import java.util.Stack;

public class T77 {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();
        while (l1 != null) {
            s1.add(l1.val);
            l1 = l1.next;
        }
        while (l2 != null) {
            s2.add(l2.val);
            l2 = l2.next;
        }
        ListNode pre = null;
        int v1 = 0, v2 = 0, add = 0;
        while (!s1.isEmpty() || !s2.isEmpty() || add != 0) {
            v1 = s1.isEmpty() ? 0 : s1.pop();
            v2 = s2.isEmpty() ? 0 : s2.pop();
            int tmp = v1 + v2 + add;
            ListNode listNode = new ListNode(tmp % 10);
            listNode.next = pre;
            pre = listNode;
            add = tmp / 10;
        }
        return pre;
    }

    public static void main(String[] args) {

    }
}
