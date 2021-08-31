package com.chouc.flink.atguigu.example.business.topn.datastream;

import java.io.Serializable;

public class TopNByHeap implements Serializable {
    AdCountWithWindow[] tree;
    int k;
    int count = 0;
    boolean fill = false;



    public TopNByHeap(int k) {
        this.tree = new AdCountWithWindow[k];
        this.k = k;
    }

    private void heapAdjust(AdCountWithWindow[] arr, int start, int end) {
        while (start <= end / 2) {
            int left = 2 * start + 1;
            int right = 2 * start + 2;
            int min = 0;
            if (left < end && right <= end) {
                if (arr[left].count <= arr[right].count)
                    min = left;
                else
                    min = right;
            }
            if (left <= end && right > end) {
                min = left;
            }
            if (left > end) {
                break;
            }

            if (arr[start].count > arr[min].count) {
                AdCountWithWindow tmp = arr[start];
                arr[start] = arr[min];
                arr[min] = tmp;
            }
            start = min;
        }
    }

    private void heapAdjust() {
        for (int start = k - 1 / 2; start >= 0; start--) {
            heapAdjust(tree, start, k - 1);
        }
    }


    void insert(AdCountWithWindow value) {
        if (!fill && count < k) {
            tree[count] = value;
            if (count - 1 == k) {
                heapAdjust();
                fill = true;
            }
            count++;
        } else {
//            System.out.println(Thread.currentThread().getName()+" "+"fill"+this);
            if (value.count > tree[0].count) {
                tree[0] = value;
                heapAdjust();
            }
        }
    }


    AdCountWithWindow[] getResult() {
        return tree;
    }

    public static void main(String[] args) {
        TopNByHeap topNByHeap = new TopNByHeap(5);
        for (int i = 1000; i > 0; i--) {
            topNByHeap.insert(new AdCountWithWindow(String.valueOf(i), Long.valueOf(i), Long.valueOf(i)));
        }
        AdCountWithWindow[] result = topNByHeap.getResult();
        for (AdCountWithWindow a : result) {
            System.out.println(a);
        }
    }

}
