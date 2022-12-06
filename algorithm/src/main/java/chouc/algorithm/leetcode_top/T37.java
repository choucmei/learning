package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T37 {
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode s = head, f = head.next;
        while (f != null && f.next != null) {
            s = s.next;
            f = f.next.next;
        }
        ListNode mid = s.next;
        s.next = null;
        ListNode l1 = sortList(head);
        ListNode l2 = sortList(mid);
        return merge(l1, l2);
    }

    public ListNode merge(ListNode n1, ListNode n2) {
        if (n1 == null) {
            return n2;
        }
        if (n2 == null) {
            return n1;
        }
        if (n1.val > n2.val) {
            n2.next = merge(n1, n2.next);
            return n2;
        } else {
            n1.next = merge(n1.next, n2);
            return n1;
        }
    }

    public static void main(String[] args) {
//        [4,2,1,3]
        ListNode head = new ListNode(4);
        head.next = new ListNode(2);
        head.next.next = new ListNode(1);
        head.next.next.next = new ListNode(3);
        T37 t37 = new T37();
        t37.sortList(head);

    }
}
