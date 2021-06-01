package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: ListNode
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/28
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
