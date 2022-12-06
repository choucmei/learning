package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T27 {
    public void reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        ListNode emptyHead = new ListNode(-1), f = emptyHead, s = emptyHead;
        emptyHead.next = head;
        while (f != null && f.next != null) {
            f = f.next.next;
            s = s.next;
        }
        ListNode next = s.next;
        s.next = null;
        s = next;

        f = head;
        s = reverse(s);
        while (s != null) {
            ListNode fNext = f.next;
            ListNode sNext = s.next;
            f.next = s;
            s.next = fNext;
            s = sNext;
            f = fNext;
        }
    }

    public ListNode reverse(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode reverse = reverse(head.next);
        head.next.next = head;
        head.next = null;
        return reverse;
    }


    public static void main(String[] args) {
        T27 t27 = new T27();
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(4);
        listNode.next.next.next.next = new ListNode(5);
        t27.reorderList(listNode);
        System.out.println(listNode);
    }


}
