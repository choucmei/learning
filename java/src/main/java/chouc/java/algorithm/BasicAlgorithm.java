package chouc.java.algorithm;

import java.util.Arrays;
import java.util.List;

public class BasicAlgorithm {

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

    public static void quicklySort(int[] a, int low, int high) {
        int start = low;
        int end = high;
        int key = a[low];
        while (end > start) {
            //从后往前比较
            while (end > start && a[end] >= key)  //如果没有比关键值小的，比较下一个，直到有比关键值小的交换位置，然后又从前往后比较
                end--;
            if (a[end] < key) {
                int temp = a[end];
                a[end] = a[start];
                a[start] = temp;
            }
            //从前往后比较
            while (end > start && a[start] <= key)//如果没有比关键值大的，比较下一个，直到有比关键值大的交换位置
                start++;
            if (a[start] > key) {
                int temp = a[start];
                a[start] = a[end];
                a[end] = temp;
            }
            //此时第一次循环比较结束，关键值的位置已经确定了。、，右边的值都比关键值大，但是两边的顺序还有可能是不一样的，进行下面的递归调用
        }
        //递归
        if (start - 1 > low) quicklySort(a, low, start - 1);//左边序列。第一个索引位置到关键值索引-1
        if (end + 1 < high) quicklySort(a, end + 1, high);//右边序列。从关键值索引+1到最后一个
    }

    public static void main(String[] args) {
        int arr[] = {7, 4, 2, 3, 9, 6};
//        insertSort(arr);
        quicklySort(arr, 0, arr.length - 1);
        Arrays.parallelSort(arr);
    }

}
