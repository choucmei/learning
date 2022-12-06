package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T39 {
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode mpHead = new ListNode(-1), pre = mpHead, cur = head;
        pre.next = head;
        while (cur != null) {
            ListNode next = cur.next;
            if (next != null && cur.val == next.val) {
                while (next != null && cur.val == next.val) {
                    next = next.next;
                }
                pre.next = next;
                cur = next;
            } else {
                pre = pre.next;
                cur = cur.next;
            }
        }
        return mpHead.next;
    }

    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(3);
        listNode.next.next.next.next = new ListNode(4);
        listNode.next.next.next.next.next = new ListNode(4);
        listNode.next.next.next.next.next.next = new ListNode(5);
        T39 t39 = new T39();
        System.out.println(t39.deleteDuplicates(listNode));
    }
}
