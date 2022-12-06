package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

public class T72 {
    public TreeNode mirrorTree(TreeNode root) {
        return dfs(root);
    }

    public TreeNode dfs(TreeNode root) {
        if (root == null) {
            return root;
        }
        TreeNode left = root.left;
        root.left = dfs(root.right);
        root.right = dfs(left);
        return root;
    }


}
