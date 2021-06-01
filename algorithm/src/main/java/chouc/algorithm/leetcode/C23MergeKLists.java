package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C23MergeKLists
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/28
 */
public class C23MergeKLists {
    public ListNode mergeKLists(ListNode[] lists) {
        int length = lists.length;
        if (length == 1){
            return lists[0];
        }
        int nextSize = length % 2 == 0 ? length / 2 : length / 2 + 1;
        ListNode[] newLists = new ListNode[nextSize];
        for (int i = 0, j = 0; i < lists.length - 1; i += 2, j++) {
            ListNode n = mergeTwoList(lists[i], lists[i + 1]);
            newLists[j] = n;
        }
        if (nextSize == 1) {
            return newLists[0];
        } else {
            if (length % 2 != 0) {
                newLists[nextSize - 1] = lists[length - 1];
            }
            return mergeKLists(newLists);
        }
    }


    private ListNode mergeTwoList(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        if (l1.val < l2.val) {
            l1.next = mergeTwoList(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoList(l1, l2.next);
            return l2;
        }
    }


    public static void main(String[] args) {
        C23MergeKLists c = new C23MergeKLists();
        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(5);
        ListNode l2 = new ListNode(1);
        l2.next = new ListNode(3);
        l2.next.next = new ListNode(4);

        ListNode l3 = new ListNode(2);
        l3.next = new ListNode(6);

        ListNode n = c.mergeKLists(new ListNode[]{});
        System.out.println(n);
        while (n != null) {
            System.out.println(n.val);
            n = n.next;
        }

    }


}
