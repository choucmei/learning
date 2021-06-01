package chouc.algorithm.leetcode;

import java.util.HashMap;

/**
 * @author chouc
 * @version V1.0
 * @Title: C1TwoSumS1
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/10/12
 */
public class C1TwoSumS1 {

    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> s = new HashMap<>();
        int c, p;
        for (int i = 0; i < nums.length; i++) {
            c = nums[i];
            p = target - c;
            if (s.get(p) != null) {
                return new int[]{s.get(p), i};
            } else {
                s.put(c, i);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        C1TwoSumS1 c = new C1TwoSumS1();
        int[] nums = new int[]{2, 3, 11, 15};
        int target = 9;
        System.out.println(c.twoSum(nums, 9));
    }
}
