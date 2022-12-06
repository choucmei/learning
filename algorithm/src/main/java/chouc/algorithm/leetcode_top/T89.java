package chouc.algorithm.leetcode_top;

public class T89 {

    public int rob(int[] nums) {
        int length = nums.length;
        if (length == 1) {
            return nums[0];
        } else if (length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        return Math.max(rob(nums, 0, length - 1), rob(nums, 1, length));
    }

    public int rob(int[] nums, int start, int len) {
        int pre = 0, cur = nums[start], sum = 0;
        for (int i = start + 1; i < len; i++) {
            sum = Math.max(cur, pre + nums[i]);
            pre = cur;
            cur = sum;
        }
        return sum;
    }

    public static void main(String[] args) {
        T89 t89 = new T89();
        System.out.println(t89.rob(new int[]{2, 3, 2}));
        System.out.println(t89.rob(new int[]{1, 2, 3, 1}));
        System.out.println(t89.rob(new int[]{2, 1, 1, 2}));
    }
}
