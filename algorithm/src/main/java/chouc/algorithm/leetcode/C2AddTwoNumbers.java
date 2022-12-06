package chouc.algorithm.leetcode;

import chouc.algorithm.common.ListNode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C1AddTwoNumbers
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/10/14
 */
public class C2AddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode n1 = l1, n2 = l2;
        int v1, v2, sum, add = 0;
        ListNode node = null;
        ListNode head = null;
        while (n1 != null || n2 != null) {
            if (n1 == null) {
                v1 = 0;
            } else {
                v1 = n1.val;
                n1 = n1.next;
            }

            if (n2 == null) {
                v2 = 0;
            } else {
                v2 = n2.val;
                n2 = n2.next;
            }
            sum = v1 + v2 + add;
            if (sum > 9) {
                add = 1;
                if (node == null) {
                    node = new ListNode(sum - 10);
                    head = node;
                } else {
                    node.next = new ListNode(sum - 10);
                    node = node.next;
                }
            } else {
                add = 0;
                if (node == null) {
                    node = new ListNode(sum);
                    head = node;
                } else {
                    node.next = new ListNode(sum);
                    node = node.next;
                }
            }
        }
        if (add != 0) {
            node.next = new ListNode(1);
        }
        return head;
    }


    public static void main(String[] args) {
        ListNode tmp;
        ListNode node1 = new ListNode(2);
        tmp = node1;
        tmp.next = new ListNode(4);
        tmp = tmp.next;
        tmp.next = new ListNode(3);

        ListNode node2 = new ListNode(5);
        tmp = node2;
        tmp.next = new ListNode(6);
        tmp = tmp.next;
        tmp.next = new ListNode(4);

        C2AddTwoNumbers c2AddTwoNumbers = new C2AddTwoNumbers();
        ListNode node = c2AddTwoNumbers.addTwoNumbers(node1, node2);
        while (node != null) {
            System.out.println(node.val);
            node = node.next;
        }

    }
}
