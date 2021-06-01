package chouc.algorithm.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chouc
 * @version V1.0
 * @Title: C141LinkedListCycle
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/10/9
 */
public class C141LinkedListCycleS2 {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    public static boolean hasCycle(ListNode head) {
        ListNode s = head;
        ListNode q = head;
        while(q != null && q.next!=null){
            s = s.next;
            q = q.next.next;
            if (s == q){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(3);
        ListNode node = head;
        ListNode c = new ListNode(2);
        node.next = c;
        node = node.next;
        node.next = new ListNode(0);
        node = node.next;
        node.next = new ListNode(-4);
        node = node.next;
        node.next = c;
        System.out.println(hasCycle(head));
    }
}
