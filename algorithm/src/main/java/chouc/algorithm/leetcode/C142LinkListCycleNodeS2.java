package chouc.algorithm.leetcode;

import chouc.algorithm.common.ListNode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C142LinkListCycleNode
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/10/10
 */
public class C142LinkListCycleNodeS2 {

    public ListNode detectCycle(ListNode head) {
        ListNode s = head;
        ListNode f = head;
        while (f != null && f.next != null) {
            s = s.next;
            f = f.next.next;
            if (s == f) {
                f = head;
                while (s != f) {
                    s = s.next;
                    f = f.next;
                }
                return s;
            }
        }
        return null;
    }

    public static void main(String[] args) {

    }
}
