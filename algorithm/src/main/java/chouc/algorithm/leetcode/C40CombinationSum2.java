package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C39CombinationSum
 * @Package chouc.java.algorithm.leetcode
 * @Description: 回溯
 * @date 2021/1/29
 */
public class C40CombinationSum2 {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> list = new ArrayList<>();
        Arrays.sort(candidates);
        dfs(list, new ArrayList<Integer>(), candidates, target, 0);
        return list;
    }

    public void dfs(List<List<Integer>> list, List<Integer> con, int[] nums, int target, int start) {
        System.out.println("start:" + start + " target:" + target + " length:" + nums.length);
        if (target == 0) {
            list.add(new ArrayList<>(con));
            return;
        }
        if (target <0){
            return;
        }
        for (int i = start; i < nums.length; i++) {
            con.add(nums[i]);
            System.out.println("start:" + start + " i:" + i + " add:" + nums[i]);
            if (i > 0 && nums[i]==nums[i-1]) {
                continue;
            }
            dfs(list, con, nums, target - nums[i], i + 1);
            con.remove(con.size() - 1);
        }
    }


    public static void main(String[] args) {
        C40CombinationSum2 c = new C40CombinationSum2();
        c.combinationSum(new int[]{2, 3, 6}, 7);
    }

}
