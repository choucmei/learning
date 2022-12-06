package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

public class T70 {
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if (root == null || subRoot == null) {
            return false;
        }
        if (root.val == subRoot.val) {
            return compare(root, subRoot) || (isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot));
        } else {
            return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
        }
    }

    public boolean compare(TreeNode root, TreeNode subRoot) {
        if (root == null && subRoot == null) {
            return true;
        }
        if (root != null && subRoot != null && root.val == subRoot.val) {
            return compare(root.right, subRoot.right) && compare(root.left, subRoot.left);
        } else {
            return false;
        }
    }
}
