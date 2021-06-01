package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C4MedianTwoSortedArrays
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/10/18
 */
public class C4MedianTwoSortedArraysS2 {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            int[] tmp = nums1;
            nums1 = nums2;
            nums2 = tmp;
        }
        int l1 = nums1.length;
        int l2 = nums2.length;
        int totalLength = l1 + l2;
        int mid = (totalLength + 1) / 2;
        int m = l1;
        int n = l2;
        int left = 0;
        int right = m;
        int i, j;
        while (left < right) {
            i = (left + right + 1) / 2;
            j = mid - i;
            System.out.println("1--- left:" + left + ",right:" + right + ",i:" + i + ",j:" + j);
            if (nums1[i - 1] > nums2[j]) {
                right = i - 1;
            } else {
                left = i;
            }
            System.out.println("2--- left:" + left + ",right:" + right + ",i:" + i + ",j:" + j);
        }
        int num1Left = left == 0 ? Integer.MIN_VALUE : nums1[left - 1];
        int num1Right = left == m ? Integer.MAX_VALUE : nums1[left];
        j = mid - left;
        int num2Left = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
        int num2Right = j == n ? Integer.MAX_VALUE : nums2[j];
        if (totalLength % 2 == 0) {
            return (Math.max(num1Left, num2Left) + Math.min(num1Right, num2Right)) * 0.5d;
        } else {
            return Math.max(num1Left, num2Left);
        }
    }


    public static void main(String[] args) {
        int[] nums1 = new int[]{6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27};
        int[] nums2 = new int[]{1, 2, 3, 4, 5};
        C4MedianTwoSortedArraysS2 c = new C4MedianTwoSortedArraysS2();
        System.out.println(c.findMedianSortedArrays(nums1, nums2));
    }
}
