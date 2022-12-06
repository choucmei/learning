package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

public class T49 {
    boolean ans = true;

    public boolean isBalanced(TreeNode root) {
        min(root);
        return ans;
    }

    public int min(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = min(root.left) + 1;
        int right = min(root.right) + 1;
        if (Math.abs(left - right) > 1) {
            ans = false;
            return Math.max(left, right);
        }
        return Math.max(left, right);
    }
}
