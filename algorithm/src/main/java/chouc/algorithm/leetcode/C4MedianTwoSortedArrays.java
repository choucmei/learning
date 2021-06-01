package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C4MedianTwoSortedArrays
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/10/18
 */
public class C4MedianTwoSortedArrays {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int l1 = nums1.length;
        int l2 = nums2.length;
        int totalLength = l1 + l2;
        if (totalLength % 2 == 0) {
            // t = 4  k = 2 , 3
            int p = calculate(nums1, 0, l1 - 1, nums2, 0, l2 - 1, (totalLength + 1) / 2);
            int a = calculate(nums1, 0, l1 - 1, nums2, 0, l2 - 1, (totalLength) / 2 + 1);
            return (p + a) / 2d;
        } else {
            // t = 3  k = 2
            return calculate(nums1, 0, l1 - 1, nums2, 0, l2 - 1, (totalLength + 1) / 2);
        }
    }

    /**
     * @param nums1
     * @param s1
     * @param e1
     * @param nums2
     * @param s2
     * @param e2
     * @param k     第几个数，不是数组下标
     * @return
     */
    private int calculate(int[] nums1, int s1, int e1, int[] nums2, int s2, int e2, int k) {
        System.out.println("s1:" + s1 + ",e1:" + e1 + ",s2:" + s2 + ",e2:" + e2 + ",k:" + k);
        int l1 = e1 - s1 + 1;
        int l2 = e2 - s2 + 1;
        if (l1 > l2) {
            return calculate(nums2, s2, e2, nums1, s1, e1, k);
        }
        if (l1 <= 0) {
            return nums2[s2 + k - 1];
        }
        if (k == 1) {
            return Math.min(nums1[s1], nums2[s2]);
        }
        int count1 = Math.min(k / 2, l1);
        int count2 = Math.min(k / 2, l2);
        int i1 = Math.min(s1 + count1 - 1, e1);
        int i2 = Math.min(s2 + count2 - 1, e2);
        if (nums1[i1] > nums2[i2]) {
            return calculate(nums1, s1, e1, nums2, s2 + count2, e2, k - count2);
        } else {
            return calculate(nums1, s1 + count1, e1, nums2, s2, e2, k - count1);
        }
    }


    public static void main(String[] args) {
        int[] nums1 = new int[]{2};
        int[] nums2 = new int[]{};
        C4MedianTwoSortedArrays c = new C4MedianTwoSortedArrays();
        System.out.println(c.findMedianSortedArrays(nums1, nums2));
    }
}
