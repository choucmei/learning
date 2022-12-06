package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T40 {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = new ListNode(-1), cur = head;
        int n1 = 0, n2 = 0, add = 0;
        while (l1 != null || l2 != null) {
            if (l1 == null) {
                n1 = 0;
            } else {
                n1 = l1.val;
                l1 = l1.next;
            }
            if (l2 == null) {
                n2 = 0;
            } else {
                n2 = l2.val;
                l2 = l2.next;
            }
            int tmp = n1 + n2 + add;
            cur.next = new ListNode(tmp % 10);
            add = tmp / 10;
            cur = cur.next;

        }
        if (add != 0) {
            cur.next = new ListNode(add);
        }
        return head.next;
    }


    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(3);
        listNode.next.next.next.next = new ListNode(4);
        listNode.next.next.next.next.next = new ListNode(4);
        listNode.next.next.next.next.next.next = new ListNode(5);
        T40 t40 = new T40();
        System.out.println(t40.addTwoNumbers(listNode, listNode));
    }
}


