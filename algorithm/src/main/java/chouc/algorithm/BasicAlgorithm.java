package chouc.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicAlgorithm {

//    稳定排序：
//    冒泡排序 — O(n²)
//    插入排序 — O(n²)
//    桶排序 — O(n); 需要 O(k) 额外空间
//    归并排序 — O(nlogn); 需要 O(n) 额外空间
//    二叉排序树排序 — O(n log n) 期望时间; O(n²)最坏时间; 需要 O(n) 额外空间
//    基数排序 — O(n·k); 需要 O(n) 额外空间
//
//    不稳定排序
//    选择排序 — O(n²)
//    希尔排序 — O(nlogn)
//    堆排序 — O(nlogn)
//    快速排序 — O(nlogn) 期望时间, O(n²) 最坏情况; 对于大的、乱数串行一般相信是最快的已知排序

    public static int[] bubbleSort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int t = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = t;
                }
            }
        }
        return array;
    }

    public static int[] insertSort(int[] array) {

        for (int i = 1; i < array.length; i++) {
            int index = array[i];
            int j;
            for (j = i - 1; j >= 0 && array[j] > index; j--) {
                array[j + 1] = array[j];
            }
            array[j + 1] = index;
        }
        return array;
    }

    public static int[] selectSort(int[] array) {

        for (int i = 0; i < array.length; i++) {
            int min = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[min]) {
                    min = j;
                }
            }
            if (min != i) {
                int t = array[min];
                array[min] = array[i];
                array[i] = t;
            }
        }
        return array;
    }


    public static void bucketSort(int[] arr) {
        int max = arr[0], min = arr[0];
        for (int a : arr) {
            if (max < a) {
                max = a;
            }
            if (min > a) {
                min = a;
            }
        }
        // 该值可根据实际情况选择
        int bucketNum = max / 10 - min / 10 + 1;

        List buckList = new ArrayList<List<Integer>>();
        // create bucket
        for (int i = 1; i <= bucketNum; i++) {
            buckList.add(new ArrayList<Integer>());
        }
        // push into the bucket
        for (int i = 0; i < arr.length; i++) {
            int index = (arr[i] - min) / 10;
            ((ArrayList<Integer>) buckList.get(index)).add(arr[i]);
        }
        ArrayList<Integer> bucket = null;
        int index = 0;
        for (int i = 0; i < bucketNum; i++) {
            bucket = (ArrayList<Integer>) buckList.get(i);
            insertSort(bucket);
            for (int k : bucket) {
                arr[index++] = k;
            }
        }
    }

    // 把桶內元素插入排序
    private static void insertSort(List<Integer> bucket) {
        for (int i = 1; i < bucket.size(); i++) {
            int temp = bucket.get(i);
            int j = i - 1;
            for (; j >= 0 && bucket.get(j) > temp; j--) {
                bucket.set(j + 1, bucket.get(j));
            }
            bucket.set(j + 1, temp);
        }
    }



    public static void mergeSort(int[] array, int start, int end, int[] temp) {
        if (start >= end) {
            return;
        }

        int mid = (start + end) / 2;

        mergeSort(array, start, mid, temp);
        mergeSort(array, mid + 1, end, temp);

        int f = start, s = mid + 1;
        int t = 0;
        while (f <= mid && s <= end) {
            if (array[f] < array[s]) {
                temp[t++] = array[f++];
            } else {
                temp[t++] = array[s++];
            }
        }

        while (f <= mid) {
            temp[t++] = array[f++];
        }

        while (s <= end) {
            temp[t++] = array[s++];
        }

        for (int i = 0, j = start; i < t; i++) {
            array[j++] = temp[i];
        }
    }

    public static void quicklySort(int[] a, int low, int high) {
        if (low > high){
            return;
        }
        int stand = a[low], start = low, end = high;
        while (start < end) {

            while (a[start] < stand) {
                start++;
            }
            while (a[end] > stand) {
                end--;
            }
            if (start < end && start < high && end > start) {
                swap(a, start, end);
            }
        }
        quicklySort(a, low, start - 1);
        quicklySort(a, end + 1, high);
    }

    private static void swap(int[] a, int start, int end) {
        int tmp = a[start];
        a[start] = a[end];
        a[end] = tmp;
    }

    public static void shellSort(int[] a, int len) {
        int step = len / 2;
        int temp;

        while (step > 0) {
            for (int i = step; i < len; ++i) {
                temp = a[i];
                int j = i - step;
                while (j >= 0 && temp < a[j]) {
                    a[j + step] = a[j];
                    j -= step;
                }
                a[j + step] = temp;
            }
            step /= 2;
        }
    }

    public static void main(String[] args) {
        int arr[] = {7, 4, 2, 3, 9, 6};
//        insertSort(arr);
        quicklySort(arr, 0, arr.length - 1);
        Arrays.parallelSort(arr);
    }

}
