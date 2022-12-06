package chouc.algorithm.leetcode;

import chouc.algorithm.common.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C19DeleteLinkList
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/12
 */
public class C19DeleteLinkList {

    public ListNode removeNthFromEnd(ListNode head, int n) {
        List<ListNode> list = new ArrayList<>();
        ListNode node = head;
        list.add(node);
        while (node.next != null) {
            ListNode c = node.next;
            list.add(c);
            node = c;
        }
        int size = list.size();
        if (size == 1) {
            return null;
        }
        if (size == n) {
            return list.get(1);
        } else {
            node = list.get(size - n - 1);
            System.out.println(node.val);
            System.out.println(node.next);
            node.next = node.next.next;
        }
        return head;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node = head;
        node.next = new ListNode(2);
//        node.next.next = new ListNode(3);
//        node.next.next.next = new ListNode(4);
//        node.next.next.next.next = new ListNode(5);
//        node.next.next.next.next.next = new ListNode(6);
        C19DeleteLinkList c = new C19DeleteLinkList();
        c.removeNthFromEnd(head, 2);
    }
}
