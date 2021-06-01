package chouc.algorithm.leetcode;

import java.util.Arrays;

/**
 * @author chouc
 * @version V1.0
 * @Title: C724PivotIndex
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/28
 */
public class C724PivotIndex {
    public int pivotIndex(int[] nums) {
        int total = Arrays.stream(nums).sum();
        int preSum = 0;
        for (int i = 0; i < nums.length; i++) {
            if (preSum *2 + nums[i] == total){
                return i;
            }
            preSum += nums[i];
        }
        return -1;
    }


    public static void main(String[] args) {

    }
}
