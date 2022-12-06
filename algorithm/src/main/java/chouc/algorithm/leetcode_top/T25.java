package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T25 {
    ListNode leftNode = null;

    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode tmpHead = new ListNode(-1), start = tmpHead, e1 = tmpHead;
        tmpHead.next = head;
        start = start.next;
        int size = right - left;
        for (; left > 1; left--) {
            e1 = start;
            start = start.next;
        }
        ListNode nTmp = reverse(start, size);
        start.next = leftNode;
        e1.next = nTmp;
        return tmpHead.next;
    }


    public ListNode reverse(ListNode head, int tmp) {
        if (head.next == null || tmp <= 0) {
            leftNode = head.next;
            return head;
        }
        ListNode reverse = reverse(head.next, tmp - 1);
        head.next.next = head;
        head.next = null;
        return reverse;
    }

    public static void main(String[] args) {
        ListNode listNode2 = new ListNode(1);
//        ListNode nlistNode2 = new ListNode(0, listNode2);
        listNode2.next = new ListNode(2);
        listNode2.next.next = new ListNode(3);
        listNode2.next.next.next = new ListNode(4);
        listNode2.next.next.next.next = new ListNode(5);
        T25 t25 = new T25();
        ListNode listNode = t25.reverseBetween(listNode2, 2, 4);
        System.out.println(listNode);
    }

}
