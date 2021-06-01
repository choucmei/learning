package chouc.algorithm.leetcode;

import java.util.Random;

/**
 * @author chouc
 * @version V1.0
 * @Title: C1206
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/13
 */
class SkipNode {
    SkipNode next;
    SkipNode down;
    int val;

    public SkipNode(SkipNode next, SkipNode down, int val) {
        this.next = next;
        this.down = down;
        this.val = val;
    }
}

public class C1206SkipList {

    SkipNode head = new SkipNode(null, null, 0);

    public C1206SkipList() {
    }

    public boolean search(int target) {
        for (SkipNode node = head; node != null; node = node.down) {
            while (node.next != null && node.next.val < target) {
                node = node.next;
            }
            if (node.next != null && node.next.val == target) {
                return true;
            }
        }
        return false;
    }

    Random rand = new Random();

    // 2^64 已经相当大了
    SkipNode[] stack = new SkipNode[64];

    public void add(int num) {
        int level = -1;
        for (SkipNode node = head; node != null; node = node.down) {
            while (node.next != null && node.next.val < num) {
                node = node.next;
            }
            stack[++level] = node;
        }
        boolean insertUp = true;
        SkipNode downNode = null;
        while (insertUp && level >= 0) {
            SkipNode node = stack[level--];
            node.next = new SkipNode(node.next, downNode, num);
            downNode = node.next;
            insertUp = (rand.nextInt() & 1) == 0;
        }
        if (insertUp) {
            head = new SkipNode(new SkipNode(null, downNode, num), head, 0);
        }
    }

    public boolean erase(int num) {
        boolean exist = false;
        for (SkipNode node = head; node != null; node = node.down) {
            while (node.next != null && node.next.val < num) {
                node = node.next;
            }
            if (node.next!=null && node.next.val <= num){
                exist = true;
                node.next = node.next.next;
            }

        }
        return exist;
    }

    public static void main(String[] args) {
    }

}
