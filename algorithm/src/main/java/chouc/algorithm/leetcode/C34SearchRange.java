package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C34SearchRange
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/17
 */
public class C34SearchRange {
    public int[] searchRange(int[] nums, int target) {
        int length = nums.length;
        if (length == 0) {
            return new int[]{-1, -1};
        }
        if (length == 1) {
            return target == nums[0] ? new int[]{0, 0} : new int[]{-1, -1};
        }
        return new int[]{search(nums, target, true), search(nums, target, false)};
    }

    private int search(int[] nums, int target, boolean lower) {
        int length = nums.length;
        int start = 0;
        int end = length - 1;
        int index = -1;
        while (start <= end) {
            int mid = (start + end) / 2;
            System.out.println(start + "-" + mid + "-" + end);
            if (nums[mid] > target) {
                end = mid - 1;
            } else if (nums[mid] < target) {
                start = mid + 1;
            } else {
                index = mid;
                if (lower) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            }
        }
        return index;
    }

    public int[] searchRange1(int[] nums, int target) {
        int leftIdx = binarySearch(nums, target, true);
        int rightIdx = binarySearch(nums, target, false) - 1;
        if (leftIdx <= rightIdx && rightIdx < nums.length && nums[leftIdx] == target && nums[rightIdx] == target) {
            return new int[]{leftIdx, rightIdx};
        }
        return new int[]{-1, -1};

    }

    public int binarySearch(int[] nums, int target, boolean lower) {
        int left = 0, right = nums.length - 1, ans = nums.length;
        while (left <= right) {
            int mid = (left + right) / 2;
            System.out.println(left + "-" + right + "-" + mid);
            if (nums[mid] > target || (lower && nums[mid] >= target)) {
                right = mid - 1;
                ans = mid;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }


    public static void main(String[] args) {
        C34SearchRange c = new C34SearchRange();
        int[] nums = new int[]{5, 7, 7, 10};
        System.out.println(c.binarySearch(nums, 8, false));
        System.out.println(c.search(nums, 8, false));
    }
}
