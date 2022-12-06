package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.ListNode;

public class T23 {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }
        int i = 0, j = lists.length - 1, nSize = (lists.length + 1) / 2;
        ListNode[] nList = new ListNode[nSize];
        for (; i < j; i++, j--) {
            nList[i] = mergeTwoLists(lists[i], lists[j]);
        }
        if (i == j) {
            nList[i] = lists[i];
        }
        return mergeKLists(nList);
    }

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        if (list1.val < list2.val) {
            list1.next = mergeTwoLists(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwoLists(list1, list2.next);
            return list2;
        }
    }

}
