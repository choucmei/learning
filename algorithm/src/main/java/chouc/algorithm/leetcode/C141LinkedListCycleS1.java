package chouc.algorithm.leetcode;

import chouc.algorithm.common.ListNode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chouc
 * @version V1.0
 * @Title: C141LinkedListCycle
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/10/9
 */
public class C141LinkedListCycleS1 {

    public static boolean hasCycle(ListNode head) {
        Set s = new HashSet();
        ListNode node = head;
        s.add(node);
        while(node.next != null){
            System.out.println(s);
            if(s.contains(node.next)){
                return true;
            }else {
                s.add(node);
            }
            node = node.next;
        }
        return false;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(3);
        ListNode node = head;
        ListNode c = new ListNode(2);
        node.next = c;
        node = node.next;
        node.next = new ListNode(0);
        node = node.next;
        node.next = new ListNode(-4);
        node = node.next;
        node.next = c;
        System.out.println(hasCycle(head));
    }
}
