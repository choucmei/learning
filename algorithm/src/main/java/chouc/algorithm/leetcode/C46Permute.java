package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C46
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/7
 */
public class C46Permute {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> rs = new ArrayList<>();
        int[] visited = new int[nums.length];
        deepFirstSearch(nums,  rs, new ArrayList<>(), visited);
        return rs;
    }

    private void deepFirstSearch(int[] nums, List<List<Integer>> rs, List<Integer> bf, int[] visited) {
        if (bf.size() == nums.length) {
            rs.add(new ArrayList<>(bf));
        }

        for (int i = 0; i < nums.length; i++) {
            if (visited[i] == 1) {
                continue;
            }
            bf.add(nums[i]);
            visited[i] = 1;
            deepFirstSearch(nums, rs, bf, visited);
            visited[i] = 0;
            bf.remove(bf.size() - 1);
        }
    }

    public static void main(String[] args) {
        C46Permute c = new C46Permute();
        System.out.println(c.permute(new int[]{1, 2, 3}));
    }
}
