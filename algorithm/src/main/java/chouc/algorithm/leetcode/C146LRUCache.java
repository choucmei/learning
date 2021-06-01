package chouc.algorithm.leetcode;

import java.util.HashMap;

/**
 * @author chouc
 * @version V1.0
 * @Title: C148LRUCache
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/9
 */
class LinkNode {
    LinkNode pre;
    LinkNode next;
    int key;
    int value;

    public LinkNode(int key, int value) {
        this.value = value;
        this.key = key;
    }

}

public class C146LRUCache {


    int capacity = 0;
    int currentSize = 0;
    HashMap<Integer, LinkNode> hashMap;
    LinkNode head;
    LinkNode tail;


    public C146LRUCache(int capacity) {
        this.capacity = capacity;
        this.hashMap = new HashMap<>(capacity);
    }

    public int get(int key) {
        LinkNode node = hashMap.getOrDefault(key, null);
        if (node == null) {
            return -1;
        }
        pre(node);
        return node.value;
    }



    public void put(int key, int value) {
        LinkNode node = null;
        if (hashMap.get(key) != null) {
            node = hashMap.get(key);
            node.value = value;
            pre(node);
        } else {
            node = new LinkNode(key, value);
            hashMap.put(key, node);
            if (head == null) {
                head = node;
                tail = node;
            } else {
                head.pre = node;
                node.next = head;
            }
            head = node;
            if (currentSize == capacity) {
                removeTail();
            } else {
                currentSize++;
            }
        }
        print("put after " + key);
    }


    private void pre(LinkNode node){
        if (node == head){
            return;
        }
        LinkNode pre = node.pre;
        LinkNode next = node.next;
        if (node == tail && node != head) {
            tail = pre;
        }
        if (pre != null) {
            pre.next = next;
        }
        if (next != null) {
            next.pre = pre;
        }
        node.next = head;
        head.pre = node;
        head = node;
    }

    private void removeTail() {
        hashMap.remove(tail.key);
        LinkNode pre = tail.pre;
        pre.next = null;
        tail.pre = null;
        tail = pre;
    }

    public void print(String t) {
        LinkNode node = head;
        System.out.print(t + ":  ");
        while (node != null) {
            System.out.print(node.key + ",");
            node = node.next;
        }
        System.out.println("-");
    }

    public static void main(String[] args) {
//        C146LRUCache c = new C146LRUCache(2);
////        [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
//        c.put(1, 1);
//        c.put(2, 1);
//        System.out.println(c.get(1));
//        c.put(3, 3);
//        System.out.println(c.get(2));
//        c.put(4, 4);
//        System.out.println(c.get(1));
//        System.out.println(c.get(3));
//        System.out.println(c.get(4));
        //["LRUCache","get","put","get","put","put","get","get"]
        //[[2],[2],[2,6],[1],[1,5],[1,2],[1],[2]]
//        C146LRUCache c = new C146LRUCache(2);
//        System.out.println(c.get(2));
//        c.put(2, 6);
//        System.out.println(c.get(1));
//        c.put(1, 5);
//        c.put(1, 2);
//        System.out.println(c.get(1));
//        System.out.println(c.get(2));

//        [[2],[2,1],[1,1],[2,3],[4,1],[1],[2]]
//        C146LRUCache c = new C146LRUCache(2);
//        c.put(2, 1);
//        c.put(1, 3);
//        c.put(2, 3);
//        c.put(4, 1);
//        System.out.println(c.get(1));
//        System.out.println(c.get(2));
//        ["LRUCache","put","put","get","get","put","get","get","get"]
//          [[2],[2,1],[3,2],[3],[2],[4,3],[2],[3],[4]]


        C146LRUCache c = new C146LRUCache(2);
        c.put(2,1);
        c.put(3,2);
        c.get(3);
        c.get(2);
        c.put(4,3);
        c.get(2);
        c.get(3);
        c.get(4);
    }
}
