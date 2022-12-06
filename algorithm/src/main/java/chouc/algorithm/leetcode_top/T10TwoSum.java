package chouc.algorithm.leetcode_top;

import java.util.HashMap;

public class T10TwoSum {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            Integer orDefault = map.getOrDefault(target - nums[i], -1);
            if (orDefault != -1 && orDefault != i) {
                return new int[]{i, orDefault};
            }
        }
        return new int[]{};
    }

    public static void main(String[] args) {
        System.out.println(4 << 1);
        int[] arr = new int[]{2, 7, 11, 15, 3};
        T10TwoSum t10TwoSum = new T10TwoSum();
        t10TwoSum.twoSum(arr, 9);
    }
}
