package chouc.algorithm.leetcode_top;

import java.util.PriorityQueue;


class MedianFinder {
    PriorityQueue<Integer> queMin;
    PriorityQueue<Integer> queMax;

    public MedianFinder() {
        queMin = new PriorityQueue<Integer>((a, b) -> (b - a));
        queMax = new PriorityQueue<Integer>((a, b) -> (a - b));
    }

    public void addNum(int num) {
        if (queMin.isEmpty() || num <= queMin.peek()) {
            queMin.offer(num);
            if (queMax.size() + 1 < queMin.size()) {
                queMax.offer(queMin.poll());
            }
        } else {
            queMax.offer(num);
            if (queMax.size() > queMin.size()) {
                queMin.offer(queMax.poll());
            }
        }
    }

    public double findMedian() {
        if (queMin.size() > queMax.size()) {
            return queMin.peek();
        }
        return (queMin.peek() + queMax.peek()) / 2.0;
    }


    public static void main(String[] args) {
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(2);
        System.out.println(medianFinder.findMedian());
        medianFinder.addNum(4);
        System.out.println(medianFinder.findMedian());
        medianFinder.addNum(3);
        System.out.println(medianFinder.findMedian());


        PriorityQueue<Integer> queMax = new PriorityQueue<>((a, b) -> (a - b));
        queMax.add(1);
        queMax.add(2);
        queMax.add(3);
        queMax.add(4);
        System.out.println(queMax.poll());
        System.out.println(queMax.poll());
        System.out.println(queMax.poll());
        System.out.println(queMax.poll());
        queMax.add(4);
        queMax.add(3);
        queMax.add(2);
        queMax.add(1);
        System.out.println(queMax.poll());
        System.out.println(queMax.poll());
        System.out.println(queMax.poll());
        System.out.println(queMax.poll());

    }
}
