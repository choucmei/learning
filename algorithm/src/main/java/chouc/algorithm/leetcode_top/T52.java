package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class T52 {
    List<List<Integer>> ans = new ArrayList<>();

    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        if (root == null){
            return ans;
        }
        pathSum(root, targetSum, new ArrayList<>());
        return ans;
    }

    public void pathSum(TreeNode root, int targetSum, List<Integer> list) {
        list.add(root.val);
        int tmp = targetSum - root.val;
        if (tmp == 0 && root.right == null && root.left == null){
            ans.add(new ArrayList<>(list));
            return;
        }
        if (root.left != null) {
            pathSum(root.left, tmp, list);
            list.remove(list.size() - 1);
        }
        if (root.right != null) {
            pathSum(root.right, tmp, list);
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
    }

}
