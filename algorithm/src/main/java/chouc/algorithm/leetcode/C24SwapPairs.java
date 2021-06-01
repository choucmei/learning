package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C24SwapPairs
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/29
 */
public class C24SwapPairs {

    public ListNode swapPairs(ListNode head) {
        ListNode node = new ListNode(0),pre,next,result;
        if (head == null || head.next ==null){
            return head;
        }
        result = head.next;
        node.next = head;
        while (node.next != null && node.next.next!= null) {
            pre = node.next;
            next = node.next.next;
            pre.next = next.next;
            node.next = next;
            node.next.next = pre;
            node = pre;
        }
        return result;
    }


    public ListNode swapPairsD(ListNode head) {
        System.out.println("n:"+head.val);
        if (head == null || head.next ==null){
            return head;
        }
        ListNode n = head.next;
        n.next = head;
        head.next = swapPairsD(n.next);
        return n;
    }

    public static void main(String[] args) {
        ListNode h = new ListNode(1);
        h.next = new ListNode(2);
        h.next.next = new ListNode(3);
        h.next.next.next = new ListNode(4);
        C24SwapPairs c = new C24SwapPairs();
        ListNode n = c.swapPairsD(h);
        Utils.printLinkList(n);
    }
}
