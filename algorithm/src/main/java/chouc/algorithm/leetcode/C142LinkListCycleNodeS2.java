package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C142LinkListCycleNode
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/10/10
 */
public class C142LinkListCycleNodeS2 {


    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }

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
