package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T24 {

    public ListNode detectCycle(ListNode head) {
        ListNode f = head, s = head;
        while (f != null && f.next != null) {
            f = f.next.next;
            s = s.next;
            if (f == s) {
                ListNode t = head;
                while (t != s){
                    s = s.next;
                    t = t.next;
                }
                break;
            }
        }
        if (f == null || f.next == null) {
            return null;
        } else {
            return s;
        }
    }


    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(4);
        listNode.next.next.next.next = new ListNode(5);
        listNode.next.next.next.next.next = listNode.next;


        ListNode listNode2 = new ListNode(1);
        ListNode nlistNode2 = new ListNode(0, listNode2);
        listNode2.next = new ListNode(2);
        listNode2.next.next = new ListNode(3);
        listNode2.next.next.next = new ListNode(4);
        listNode2.next.next.next.next = new ListNode(5);
        listNode2.next.next.next.next.next = listNode.next;
        T24 t24 = new T24();
//        PrintUtils.printListNode(nlistNode2);
//        PrintUtils.printListNode(listNode);
        System.out.println(t24.detectCycle(listNode));
        System.out.println(t24.detectCycle(nlistNode2));
    }
}
