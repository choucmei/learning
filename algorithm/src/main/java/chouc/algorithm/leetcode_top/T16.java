package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.Utils;

public class T16 {
    public void merge(int[] nums1, int m, int[] nums2, int n) {

        int index = nums1.length - 1, i = m - 1, j = n - 1;
        while (index >= 0) {
            if (i >= 0 && j >= 0) {
                if (nums1[i] > nums2[j]) {
                    nums1[index] = nums1[i];
                    i--;
                } else {
                    nums1[index] = nums2[j];
                    j--;
                }
            } else if (j < 0) {
                return;
            } else {
                nums1[index] = nums2[j];
                j--;
            }
            index--;
        }
    }

    public static void main(String[] args) {
//        [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
        T16 t16 = new T16();
        int[] ints = {2, 0};
        t16.merge(ints, 1, new int[]{1}, 1);
        Utils.printArray(ints);
    }
}
