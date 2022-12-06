package chouc.algorithm.leetcode_top;

public class T62 {
    public boolean canJump(int[] nums) {
        int[] dp = new int[nums.length];
        int maxIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (maxIndex >= i) {
                maxIndex = Math.max(maxIndex, i + nums[i]);
            }
            if (maxIndex > nums.length-1){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        T62 t62 = new T62();
//        System.out.println(t62.canJump(new int[]{2, 3, 1, 1, 4}));
//        System.out.println(t62.canJump(new int[]{3, 2, 1, 0, 4}));
//        System.out.println(t62.canJump(new int[]{0, 2, 3}));
        System.out.println(t62.canJump(new int[]{0}));

    }
}
