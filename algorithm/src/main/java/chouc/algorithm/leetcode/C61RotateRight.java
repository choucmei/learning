package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C61RotateRight
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/3/11
 */
public class C61RotateRight {
    public ListNode rotateRight(ListNode head, int k) {
        if (k == 0 || head == null || head.next == null) {
            return head;
        }
        int listLength = 1;
        ListNode tail = head;
        while (tail.next != null) {
            listLength++;
            tail = tail.next;
        }
        ListNode node = head;
        int lastNode = k % listLength;
        if (lastNode == 0) {
            return head;
        }
        int index = listLength - lastNode;
        for (int i = 1; i < index; i++) {
            node = node.next;
        }
        ListNode keyNode = node.next;
        tail.next = head;
        node.next = null;
        return keyNode;
    }

//    private ListNode reverse(ListNode node) {
//        if (node == null || node.next == null) {
//            return node;
//        }
//        ListNode next = node.next;
//        ListNode head = reverse(node.next);
//        node.next = null;
//        next.next = node;
//        return head;
//    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node = head;
        node.next = new ListNode(2);
        node.next.next = new ListNode(3);
        node.next.next.next = new ListNode(4);
        node.next.next.next.next = new ListNode(5);
        C61RotateRight c = new C61RotateRight();
//        Utils.printLinkList(head);
//        Utils.printLinkList(c.reverse(head));
        Utils.printLinkList(c.rotateRight(head, 5));

    }
}
