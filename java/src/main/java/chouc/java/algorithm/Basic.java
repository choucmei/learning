package chouc.java.algorithm;

/**
 * @author chouc
 * @version V1.0
 * @Title: Basic
 * @Package chouc.java.algorithm
 * @Description:
 * @date 10/11/19
 */
public class Basic {

    private static void swap(int[] array, int src, int dst) {
        int tmp = array[src];
        array[src] = array[dst];
        array[dst] = tmp;
    }

    private static void bubbleSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[i] > a[j]) {
                    swap(a, i, j);
                }
            }
        }
    }

    private static void selectSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + i; j < a.length; j++) {
                if (a[j] < a[min]) {
                    min = j;
                }
            }
            swap(a, i, min);
        }
    }

    private static void insertSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[i] > a[j]) {

                }
            }
        }
    }

    private static void insertSort02(int[] arr, int start, int end) {
        int startOrigin = start;
        int endOrigin = end;
        int mid = arr[start];
        while (start < end) {
            while (start < end && arr[end] >= mid) {
                end--;
            }
            if (start < end) {
                swap(arr, start, end);
            }
            while (start < end && arr[start] <= mid) {
                start++;
            }
            if (start < end) {
                swap(arr, start, end);
            }
        }
        if (start - 1 > startOrigin) {
            insertSort02(arr, startOrigin, start - 1);
        }
        if (end + 1 < endOrigin) {
            insertSort02(arr, end + 1, endOrigin);
        }
    }


    public static void main(String[] args) {
        int arr[] = {6, 2, 7, 3, 8, 9};
        insertSort02(arr, 0, arr.length - 1);
        System.out.println(String.valueOf(arr));
    }
}
