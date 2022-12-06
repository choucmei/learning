package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.Utils;

import java.util.Arrays;

public class T82 {
    public int[] intersection(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int i = 0, j = 0, len1 = nums1.length, len2 = nums2.length, count = 0;
        int[] intersection = new int[len1 + len2];
        while (i < len1 && j < len2) {
            if (nums1[i] == nums2[j]) {
                intersection[count++] = nums1[i];
                int nI = i + 1;
                while (nI < len1 && nums1[nI] == nums1[i]) {
                    nI++;
                }
                i = nI;
                j++;
            } else if (nums1[i] < nums2[j]) {
                int nI = i + 1;
                while (nI < len1 && nums1[nI] == nums1[i]) {
                    nI++;
                }
                i = nI;
            } else {
                int nJ = j + 1;
                while (nJ < len2 && nums2[nJ] == nums2[j]) {
                    nJ++;
                }
                j = nJ;
            }
        }
        int[] ret = Arrays.copyOf(intersection, count);
        return ret;
    }

    public static void main(String[] args) {
        T82 t82 = new T82();
        Utils.printArray(t82.intersection(new int[]{1, 2, 2, 1}, new int[]{2, 2}));
    }
}
