package chouc.algorithm.leetcode;

import java.util.*;

/**
 * @author chouc
 * @version V1.0
 * @Title: C460LFU
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/3/30
 */
public class C460LFU {


    static class LFUCache {
        LinkedHashMap<Integer, Node> cache = new LinkedHashMap<>();
        int capacity = 0;

        static class Node implements Comparable<Node> {
            int value;
            int count;
            long time;

            public Node(int value, int count, long time) {
                this.value = value;
                this.count = count;
                this.time = time;
            }


            @Override
            public int compareTo(Node o) {
                int compare = Integer.compare(this.count, o.count); //在数目相同的情况下 比较时间
                if (compare == 0) {
                    return Long.compare(this.time, o.time);
                }
                return compare;
            }
        }

        public LFUCache(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            Node node = cache.get(key);
            node.count += 1;
            node.time = System.nanoTime();
            sort();
            return node.value;
        }

        public void put(int key, int value) {
            Node node = cache.get(key);
            if (node == null) {
                if (this.cache.size() >= this.capacity) {
                    int leastKey = removeLeastCount();
                    cache.remove(leastKey);
                }
                cache.put(key, new Node(value, 1, System.nanoTime()));
            } else {
                node.value = value;
                node.count += 1;
                node.time = System.nanoTime();
            }
            sort();
        }

        private void sort() {
            System.out.println("sort");
            List<Map.Entry<Integer, Node>> list = new ArrayList(cache.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<Integer, Node>>() {
                @Override
                public int compare(Map.Entry<Integer, Node> o1, Map.Entry<Integer, Node> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            cache.clear();
            for (Map.Entry<Integer, Node> n : list) {
                cache.put(n.getKey(), n.getValue());
            }
        }

        private int removeLeastCount() {
            System.out.println("re");
            Map.Entry<Integer, Node> node = Collections.min(cache.entrySet(), new Comparator<Map.Entry<Integer, Node>>() {
                @Override
                public int compare(Map.Entry<Integer, Node> o1, Map.Entry<Integer, Node> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            return node.getKey();
        }
    }

    public static void main(String[] args) {
        LFUCache lruCache = new LFUCache(5);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        lruCache.put(3, 3);
        lruCache.put(4, 4);
        lruCache.put(5, 5);
        lruCache.put(6, 6);
        lruCache.get(2);
        lruCache.get(2);
        lruCache.get(3);
        lruCache.get(6);
        //重新put节点3
        lruCache.put(3, 33);
        lruCache.put(7, 7);

        final Map<Integer, LFUCache.Node> caches = (Map<Integer, LFUCache.Node>) lruCache.cache;
        for (Map.Entry<Integer, LFUCache.Node> nodeEntry : caches.entrySet()) {
            System.out.println(nodeEntry.getValue().value+" "+nodeEntry.getValue().count+" "+nodeEntry.getValue().time/100000);
        }
    }

}
