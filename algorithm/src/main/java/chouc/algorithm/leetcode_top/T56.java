package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T56 {
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode nHead = new ListNode(-1), f = head;
        nHead.next = head;
        ListNode pre = nHead;
        while (f != null && f.next != null) {
            ListNode tmp = f.next;
            f.next = tmp.next;
            tmp.next = f;
            pre.next = tmp;
            pre = f;
            f = f.next;
        }
        return nHead.next;
    }


    public static void main(String[] args) {
        T56 t56 = new T56();
        System.out.println(t56);
    }


}
