package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C21MergeTwoLists
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/13
 */
public class C21MergeTwoLists {
    static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode newNode = new ListNode(0);
        if (l1 == null){
            return l2;
        }
        if (l2 == null){
            return l1;
        }
        if (l1.val < l2.val){
            l1.next = mergeTwoLists(l1.next,l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1,l2.next);
            return l2;
        }
    }


    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(2);
        l1.next.next = new ListNode(3);
        ListNode l2 = new ListNode(1);
        l2.next = new ListNode(2);
        l2.next.next = new ListNode(3);
        C21MergeTwoLists c = new C21MergeTwoLists();
        ListNode n = c.mergeTwoLists(l1,l2);
        while (n != null){
            System.out.println(n.val);
            n = n.next;
        }
    }
}

