package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

public class T28 {
    int max = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        max = Math.max(tree(root),max);
        return max;
    }

    public int tree(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        }
        int left = Math.max(0, tree(treeNode.left));
        int right = Math.max(0, tree(treeNode.right));
        max = Math.max(treeNode.val + right + left, this.max);
        return treeNode.val + Math.max(right, left);
    }

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        treeNode.left = new TreeNode(2);
        treeNode.right = new TreeNode(3);


        TreeNode treeNode2 = new TreeNode(-10);
        treeNode2.left = new TreeNode(9);
        treeNode2.right = new TreeNode(20);
        treeNode2.right.left = new TreeNode(15);
        treeNode2.right.right = new TreeNode(7);


        TreeNode treeNode3 = new TreeNode(-10);

        TreeNode treeNode4 = new TreeNode(2);
        treeNode4.right = new TreeNode(-1);
        treeNode4.left = new TreeNode(-2);

//        System.out.println(new T28().maxPathSum(treeNode));
//        System.out.println(new T28().maxPathSum(treeNode2));
//        System.out.println(new T28().maxPathSum(treeNode3));
        System.out.println(new T28().maxPathSum(treeNode4));


    }
}
