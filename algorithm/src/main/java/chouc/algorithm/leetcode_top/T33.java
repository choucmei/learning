package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T33 {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode emptyNode = new ListNode(-1), f = emptyNode, s = emptyNode;
        emptyNode.next = head;
        for (int i = 0; i < n; i++) {
            f = f.next;
        }
        while (f != null && f.next != null) {
            f = f.next;
            s = s.next;
        }
        ListNode deleteNode = s.next;
        s.next = deleteNode.next;
        return emptyNode.next;
    }


    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(4);
        listNode.next.next.next.next = new ListNode(5);


        T33 t33 = new T33();
        System.out.println(t33.removeNthFromEnd(listNode, 2));
    }
}
