package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C18
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/11
 */
public class C18FourSum {

    public List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        for (int i : nums) {
            System.out.print(i + ",");
        }
        System.out.println("");
        int length = nums.length;
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < length - 3; i++) {
            if (i > 0 && nums[i - 1] == nums[i]) {
                continue;
            }
            if (nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) {
                break;
            }
            if (nums[i] + nums[length - 3] + nums[length - 2] + nums[length - 1] < target) {
                continue;
            }
            for (int j = i + 1; j < nums.length - 2; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                if (nums[i] + nums[j] + nums[i + 2] + nums[i + 3] > target) {
                    break;
                }
                if (nums[i] + nums[j] + nums[length - 2] + nums[length - 1] < target) {
                    continue;
                }
                int left = j + 1, right = length - 1;
                while (left < right) {
                    System.out.println(i + " " + j + " " + left + " " + right);
                    int sum = nums[i] + nums[j] + nums[left] + nums[right];
                    if (sum == target) {
                        list.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                        while (left < right && nums[left] == nums[left + 1]) {
                            left++;
                        }
                        while (left < right && nums[right] == nums[right - 1]) {
                            right--;
                        }
                        left++;
                        right--;
                    } else if (sum > target) {
                        right--;
                    } else {
                        left++;
                    }
                }
            }
        }
        return list;
    }


    public static void main(String[] args) {
        int[] nums = new int[]{-1,-5,-5,-3,2,5,0,4};
        C18FourSum c = new C18FourSum();
        System.out.println(c.fourSum(nums, 0));
    }
}
