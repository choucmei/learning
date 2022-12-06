package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T5ReverseKGroup {
    public ListNode reverseKGroup(ListNode head, int k) {
        if (k == 1) {
            return head;
        }
        ListNode cur = new ListNode(0), tail = cur, newHead = null;
        cur.next = head;
        while (tail != null) {
            for (int i = k; i > 0 && tail != null; i--) {
                tail = tail.next;
            }
            if (tail != null) {
                ListNode curTail = tail.next;
                ListNode curHead = cur.next;
                tail.next = null;
                ListNode curNewHead = reverse(curHead);
                curHead.next = curTail;
                cur.next = curNewHead;
                cur = curHead;
                tail = curHead;
                if (newHead == null) {
                    newHead = curNewHead;
                }
            }
        }
        return newHead;
    }


    public ListNode reverse(ListNode head) {
        if (head == null || head.next == null){
            return head;
        }
        ListNode reverseHead = reverse(head.next);
        head.next.next = head;
        head.next = null;
        return reverseHead;
    }

    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(4);
        listNode.next.next.next.next = new ListNode(5);
        T5ReverseKGroup t5 = new T5ReverseKGroup();
        ListNode listNode1 = t5.reverseKGroup(listNode, 2);

    }
}
