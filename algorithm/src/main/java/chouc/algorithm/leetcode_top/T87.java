package chouc.algorithm.leetcode_top;

import java.util.ArrayList;
import java.util.List;

public class T87 {
    public List<Integer> findDuplicates(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[nums[i] - 1] == nums[i] && nums[i] - 1 != i && nums[i] != 0) {
                ret.add(nums[i]);
            }
        }
        return ret;
    }

    private void swap(int[] nums, int i, int i1) {
        int temp = nums[i];
        nums[i] = nums[i1];
        nums[i1] = temp;
    }

    public static void main(String[] args) {
        T87 t87 = new T87();
        System.out.println(t87.findDuplicates(new int[]{4, 3, 2, 7, 8, 2, 3, 1}));
        System.out.println(t87.findDuplicates(new int[]{1, 1, 2}));
        System.out.println(t87.findDuplicates(new int[]{5, 4, 6, 7, 9, 3, 10, 9, 5, 6}));
    }
}
