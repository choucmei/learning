package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C46PermuteUnique
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/8
 */
public class C47PermuteUnique {
    public List<List<Integer>> permuteUnique(int[] nums) {
        int[] has = new int[nums.length];
        List<List<Integer>> rs = new ArrayList<>();
        Arrays.sort(nums);
        dfs(nums, has, new ArrayList<>(), rs);
        return rs;
    }

    private void dfs(int[] num, int[] has, List<Integer> ls, List<List<Integer>> rs) {
        if (ls.size() == num.length) {
            rs.add(new ArrayList<>(ls));
            return;
        }
        for (int i = 0; i < num.length; i++) {
            if (has[i] == 1 || (i > 0 && num[i - 1] == num[i] && has[i-1]==0)) {
                continue;
            } else {
                has[i] = 1;
                ls.add(num[i]);
                dfs(num, has, ls, rs);
                has[i] = 0;
                ls.remove(ls.size() - 1);
            }
        }
    }

    public static void main(String[] args) {
        C47PermuteUnique c = new C47PermuteUnique();
        System.out.println(c.permuteUnique(new int[]{1, 2, 3}));
        System.out.println(c.permuteUnique(new int[]{1, 1, 3}));
    }
}
