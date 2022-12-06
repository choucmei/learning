package chouc.algorithm.leetcode_top;

public class T76 {
    public void rotate(int[] nums, int k) {
        int len = nums.length;
        int[] nArr = new int[len];
        for (int i = 0; i < len; i++) {
            nArr[(i + k) % len] = nums[i];
        }
        System.arraycopy(nArr, 0, nums, 0, len);
    }
}
