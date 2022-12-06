package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;
import chouc.algorithm.common.Utils;

import javax.rmi.CORBA.Util;

public class T92 {
    public ListNode partition(ListNode head, int x) {
        ListNode vHead = new ListNode(Integer.MIN_VALUE);
        vHead.next = head;
        ListNode node = vHead;
        while (node.next != null && node.next.val < x) {
            node = node.next;
        }
        ListNode keyNode = node;
        while (node != null && node.next != null) {
            if (node.next.val < x) {
                ListNode tmp = node.next;
                node.next = node.next.next;
                ListNode tmp2 = keyNode.next;
                keyNode.next = tmp;
                tmp.next = tmp2;
                keyNode = keyNode.next;
            } else {
                node = node.next;
            }
        }
        return vHead.next;
    }

    public static void main(String[] args) {
        ListNode list = ListNode.parse("[1,4,3,0,2,5,2]");
        T92 t92 = new T92();
        Utils.printLinkList(t92.partition(list, 3));
    }
}
