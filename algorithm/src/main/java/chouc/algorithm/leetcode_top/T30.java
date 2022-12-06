package chouc.algorithm.leetcode_top;

public class T30 {
    public int search(int[] nums, int target) {
        int length = nums.length, i = 0, j = length - 1, mid;
        while (i <= j) {
            mid = (i + j) / 2;
            if (nums[mid] > target) {
                j = mid - 1;
            } else if (nums[mid] < target) {
                i = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
