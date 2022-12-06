package chouc.algorithm.leetcode_top;

public class T58 {
    public int maxProduct(int[] nums) {
        int length = nums.length, ans = nums[0];
        int[] maxF = new int[length];
        int[] minF = new int[length];
        System.arraycopy(nums, 0, maxF, 0, length);
        System.arraycopy(nums, 0, minF, 0, length);
        for (int i = 1; i < nums.length; i++) {
            maxF[i] = Math.max(Math.max(nums[i], nums[i] * maxF[i - 1]), nums[i] * minF[i - 1]);
            minF[i] = Math.min(Math.min(nums[i], nums[i] * minF[i - 1]), nums[i] * maxF[i - 1]);
        }
        for (int i : maxF) {
            ans = Math.max(ans, i);
        }
        return ans;
    }


    public static void main(String[] args) {
        T58 t58 = new T58();
        System.out.println(t58.maxProduct(new int[]{2, 3, -2, 4, -1}));
        System.out.println(t58.maxProduct(new int[]{-2, 0, -1}));
    }
}
