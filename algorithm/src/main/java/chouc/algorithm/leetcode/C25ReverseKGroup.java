package chouc.algorithm.leetcode;

import chouc.algorithm.common.ListNode;
import chouc.algorithm.common.Utils;

/**
 * @author chouc
 * @version V1.0
 * @Title: C25ReverseKGroup
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/30
 */
public class C25ReverseKGroup {
    public ListNode reverseKGroup(ListNode head, int k) {
        if (k == 1 || head == null){
            return head;
        }
        ListNode nNode = new ListNode(0);
        int j = 0, i = 0;
        ListNode node = head, pre = null,left = null,tail = null;
        while (j < k && node != null) {
            node = node.next;
            j++;
        }
        if (j < k){
            return head;
        }
        while (i < k && head != null) {
            if (nNode.next != null) {
                left = head.next;
                pre = nNode.next;
                nNode.next = head;
                head.next = pre;
                head = left;
            } else {
                nNode.next = head;
                tail = head;
                head = head.next;
            }
            i++;
        }
        tail.next = reverseKGroup(left,k);
        return nNode.next;
    }

    public static void main(String[] args) {
        ListNode h = new ListNode(1);
        h.next = new ListNode(2);
        h.next.next = new ListNode(3);
        h.next.next.next = new ListNode(4);
        h.next.next.next.next = new ListNode(5);
        C25ReverseKGroup c = new C25ReverseKGroup();
        ListNode n = c.reverseKGroup(h,4);
        Utils.printLinkList(n);
    }


}
