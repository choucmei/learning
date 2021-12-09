package chouc.algorithm.leetcode_top;

public class PrintUtils {

    public static void printListNode(ListNode head) {
        ListNode node = head;
        while (node != null) {
            System.out.print(node.val + " -> ");
            node = node.next;
        }
        System.out.println("null ");
    }
}
