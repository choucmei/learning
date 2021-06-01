package chouc.algorithm.leetcode;

import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: Utils
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/29
 */
public class Utils {
    public static void printLinkList(ListNode n) {
        while (n != null) {
            System.out.println(n.val);
            n = n.next;
        }
    }

    public static void printArray(int[] n) {
        System.out.printf("[");
        for (int i = 0; i < n.length; i++) {
            System.out.printf(n[i] + ",");
        }
        System.out.println("]");
    }

    public static void printStringArray(String[] n) {
        System.out.printf("[");
        for (int i = 0; i < n.length; i++) {
            System.out.printf(n[i] + ",");
        }
        System.out.println("]");
    }

    public static void printListInList(List<List<String>> n) {
        System.out.println("[");
        for (List<String> l : n) {
            System.out.print("[");
            for (Object o : l) {
                System.out.print(o + ",");
            }
            System.out.println("]");
        }
        System.out.println("]");
    }

    public static void printArrayInArray(int[][] n) {
        for (int[] arr : n) {
            System.out.printf("[");
            for (int i = 0; i < arr.length; i++) {
                System.out.printf(arr[i] + ",");
            }
            System.out.println("]");
        }
    }


    public static void printBooleanArray(boolean[] n) {
        System.out.printf("[");
        for (int i = 0; i < n.length; i++) {
            System.out.printf(n[i] + ",");
        }
        System.out.println("]");
    }

}
