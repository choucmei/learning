package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.Utils;

import java.util.Random;

public class T67 {
    int[] nums;
    int[] original;

    int length;

        public T67(int[] nums) {
            this.nums = nums;
            this.original = new int[nums.length];
            System.arraycopy(nums, 0, original, 0, nums.length);
            length = nums.length;
        }

        public int[] reset() {
            System.arraycopy(original, 0, nums, 0, nums.length);
            return nums;
        }

    public int[] shuffle() {
        for (int i = length - 1; i > 0; i--) {
            int change = new Random().nextInt(i + 1);
            int tmp = nums[i];
            nums[i] = nums[change];
            nums[change] = tmp;
        }
        return nums;
    }

    public static void main(String[] args) {
        T67 t67 = new T67(new int[]{1, 2, 3});
        for (int i = 0; i < 10; i++) {
            Utils.printArray(t67.shuffle());
            Utils.printArray(t67.reset());
            System.out.println("---------------------");
        }

    }
}
