package chouc.algorithm.leetcode_top;

public class T15 {
//    输入：nums = [4,5,6,7,0,1,2], target = 0
//    输入：nums = [1,2,4,5,6,7,8,9,0], target = 2
//    输入：nums = [9,0,1,2,4,5,6,7,8], target = 2


//    输出：4

    public int search(int[] nums, int target) {
        int start = 0, end = nums.length - 1, mid;
        while (start <= end && end >= 0 && start < nums.length) {
            mid = (start + end) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            // order
            if (nums[start] < nums[end]) {
                if (target < nums[start] || target > nums[end]) {
                    return -1;
                }
                if (target < nums[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else {
                if (nums[mid] >= nums[start]) {
                    if (target < nums[mid] && target >= nums[start]) {
                        end = mid - 1;
                    } else {
                        start = mid + 1;
                    }
                } else {
                    if (target > nums[mid] && target <= nums[end]) {
                        start = mid + 1;
                    } else {
                        end = mid - 1;
                    }
                }
            }
        }
        return -1;
    }


    public static void main(String[] args) {
        T15 t15 = new T15();
        System.out.println(t15.search(new int[]{5,1, 3}, 3));
    }
}
