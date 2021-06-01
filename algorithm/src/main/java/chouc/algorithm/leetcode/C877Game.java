package chouc.algorithm.leetcode;

import java.util.Arrays;

/**
 * @author chouc
 * @version V1.0
 * @Title: C877Game
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/8
 */
public class C877Game {
    public boolean stoneGame(int[] piles) {
        int total = Arrays.stream(piles).sum();
        int half = total / 2;
        return dfs(piles, half, 0, piles.length - 1, 0, true);
    }

    private boolean dfs(int[] nums, int half, int start, int end, int sum, boolean first) {
        if (sum > half) {
            return true;
        }
        if (start > end) {
            return false;
        }
        if (first || nums[start] == nums[end]) {
            return dfs(nums, half, start + 1, end, sum + nums[start], !first) || dfs(nums, half, start, end - 1, sum + nums[end], !first);
        } else {
            if (nums[start] > nums[end]) {
                return dfs(nums, half, start, end - 1, sum, !first);
            } else {
                return dfs(nums, half, start + 1, end, sum, !first);
            }
        }
    }

    public static void main(String[] args) {
        C877Game c = new C877Game();
        System.out.println(c.stoneGame(new int[]{3, 7, 2, 3}));
    }
}
