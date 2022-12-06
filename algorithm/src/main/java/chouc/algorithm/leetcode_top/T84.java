package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T84 {
    ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        ListNode l1 = headA, l2 = headB;
        while (l1 != l2) {
            l1 = l1 == null ? l2 : l1.next;
            l2 = l2 == null ? l1 : l2.next;
        }
        return l1;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(2);
        l1.next.next = new ListNode(3);
        l1.next.next.next = new ListNode(6);

        ListNode l2 = new ListNode(4);
        l2.next = new ListNode(5);
        l2.next.next = new ListNode(6);


        T84 t84 = new T84();
        System.out.println(t84.getIntersectionNode(l1, l2));
    }
}
