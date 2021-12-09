package chouc.algorithm.leetcode_top;

/**
 * 206. 反转链表  https://leetcode-cn.com/problems/reverse-linked-list/
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
 */
public class T1ReverseList {

    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        PrintUtils.printListNode(head);
        return newHead;
    }


    public static void main(String[] args) {
        T1ReverseList t1ReverseList = new T1ReverseList();
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(4);
        PrintUtils.printListNode(listNode);
        ListNode listNode1 = t1ReverseList.reverseList(listNode);
        PrintUtils.printListNode(listNode1);
    }
}
