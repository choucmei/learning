package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C55CanJump
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/26
 */
public class C55CanJump {
    public boolean canJump(int[] nums) {
        int length = nums.length;
        int maxLength = 0;
        for (int i = 0; i < length; i++) {
            if (maxLength >= i) {
                maxLength = Math.max(maxLength,i+nums[i]);
                if (maxLength>=length-1){
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        C55CanJump c = new C55CanJump();
        System.out.println(c.canJump(new int[]{0}));
    }
}
