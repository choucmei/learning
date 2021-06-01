package chouc.algorithm.leetcode_hot;

/**
 * @author chouc
 * @version V1.0
 * @Title: H4SortedArraysMid
 * @Package chouc.java.algorithm.leetcode_hot
 * @Description:
 * @date 2021/4/13
 */
public class H4SortedArraysMid {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int length1 = nums1.length, length2 = nums2.length, total = length1 + length2;
        boolean single = (length1 + length2) % 2 != 0;
        int leftTotal = (total + 1) / 2;
        int middle1 = length1 / 2;
        int middle2 = leftTotal - middle1 - 2;
        int rightMin = Math.min(nums1[middle1 + 1], nums2[middle2 + 1]);
        int leftMax = Math.max(nums1[middle1], nums2[middle2]);
        System.out.println("middle1:" + middle1 + " middle2:" + middle2 + " leftTotal:" + leftTotal);
        while (leftMax > rightMin) {
            if (nums1[middle1 + 1] < nums2[middle2]) {
                middle1 += 1;
            } else {
                middle1 -= 1;
            }
            middle2 = leftTotal - middle1 - 2;
            System.out.println("middle1:" + middle1 + " middle2:" + middle2);
            if (middle1 + 1 >= length1) {
                rightMin = nums2[middle2 + 1];
            } else if (middle2 + 1 >= length2) {
                rightMin = Math.min(nums1[middle1 + 1], nums2[middle2 + 1]);
            } else {
                rightMin = Math.min(nums1[middle1 + 1], nums2[middle2 + 1]);
            }
            leftMax = Math.max(nums1[middle1], nums2[middle2]);
        }
        System.out.println(single);
        if (single) {
            return leftMax;
        } else {
            System.out.println(leftMax + " " + rightMin);
            return (leftMax + rightMin) / 2d;
        }
    }


    public static void main(String[] args) {
        H4SortedArraysMid h = new H4SortedArraysMid();
        int arr1[] = new int[]{1, 2, 3, 4};
        int arr2[] = new int[]{5, 7, 8, 9, 10};
        System.out.println(h.findMedianSortedArrays(arr1, arr2));
    }
}
