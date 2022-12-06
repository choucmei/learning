package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

public class T18LowestCommonAncestor {
    TreeNode ans = null;

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        dfs(root, p, q);
        return ans;
    }

    public boolean dfs(TreeNode root, TreeNode q, TreeNode p) {
        if (root == null) {
            return false;
        }
        boolean left = dfs(root.left, q, p);
        boolean right = dfs(root.right, q, p);
        boolean current = root == q || root == p;
        if ((left && right) || current && (left || right)) {
            if (ans == null) {
                ans = root;
            }
        }
        return current || left || right;
    }


    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(5);
        root.left.left = new TreeNode(6);
        root.left.right = new TreeNode(2);
        root.left.right.left = new TreeNode(7);
        root.left.right.right = new TreeNode(4);

        root.right = new TreeNode(1);
        root.right.left = new TreeNode(0);
        root.right.right = new TreeNode(8);

        T18LowestCommonAncestor t18LowestCommonAncestor = new T18LowestCommonAncestor();
        System.out.println(t18LowestCommonAncestor.lowestCommonAncestor(root, root.left, root.left.right));


    }
}
