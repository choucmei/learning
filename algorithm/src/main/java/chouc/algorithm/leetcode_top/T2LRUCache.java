package chouc.algorithm.leetcode_top;

import java.util.HashMap;
import java.util.Map;

public class T2LRUCache {

    static class Node {
        int key;
        int value;
        Node pre;
        Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                "key=" + key +
                ", value=" + value +
                '}';
        }
    }

    static class LRUCache {
        int capacity;
        int currentSize;
        Map<Integer, Node> container = new HashMap();
        Node head;
        Node tail;

        public LRUCache(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            Node orDefault = container.getOrDefault(key, null);
            if (orDefault == null) {
                return -1;
            } else {
                head(orDefault);
                return orDefault.value;
            }
        }

        public void put(int key, int value) {
            Node node = container.get(key);
            if (node != null) {
                node.value = value;
                head(node);
            } else {
                Node newNode = new Node(key, value);
                if (currentSize < capacity) {
                    if (head == null) {
                        head = newNode;
                        tail = head;
                    } else {
                        newNode.next = head;
                        head.pre = newNode;
                        head = newNode;
                    }
                    container.put(key, head);
                    currentSize++;
                } else {
                    removeLast();
                    newNode.next = head;
                    head.pre = newNode;
                    container.put(key, head);
                    head = newNode;
                }
            }
        }

        private void head(Node node) {
            if (node == head){
                return;
            }
            Node pre = node.pre;
            Node next = node.next;
            if (node == tail) {
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

        private void removeLast() {
            System.out.println(tail);
            container.remove(tail.key);
            tail.pre.next = null;
            tail = tail.pre;
        }


    }


    public static void main(String[] args) {
        LRUCache lRUCache = new LRUCache(2);
        lRUCache.put(1, 1); // 缓存是 {1=1}
        lRUCache.put(2, 2); // 缓存是 {1=1, 2=2}
        lRUCache.get(1);    // 返回 1
        lRUCache.put(3, 3); // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
        lRUCache.get(2);    // 返回 -1 (未找到)
        lRUCache.put(4, 4); // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
        lRUCache.get(1);    // 返回 -1 (未找到)
        lRUCache.get(3);    // 返回 3
        lRUCache.get(4);    // 返回 4
    }
}
