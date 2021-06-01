package chouc.algorithm.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chouc
 * @version V1.0
 * @Title: C142LinkListCycleNode
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/10/10
 */
public class C142LinkListCycleNodeS1 {


    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    public ListNode detectCycle(ListNode head) {
        Set<ListNode> set = new HashSet<>();
        if (head== null){
            return null;
        }
        ListNode node = head;
        set.add(node);
        while (node != null) {
            if (set.contains(node.next)){
                return node.next;
            }else {
                node = node.next;
                set.add(node);
            }
        }
        return null;
    }

    public static void main(String[] args) {

    }
}
