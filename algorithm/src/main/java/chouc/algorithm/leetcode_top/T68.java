package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

public class T68 {
    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) {
            return root;
        }
        if (root.val > key) {
            root.left = deleteNode(root.left, key);
            return root;
        } else if (root.val < key) {
            root.right = deleteNode(root.right, key);
            return root;
        } else {
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            TreeNode cur = root.right, pre = null;
            while (cur.left != null) {
                pre = cur;
                cur = cur.left;
            }
            cur.left = root.left;
            cur.right = root.right;
            if (pre != null) {
                pre.left = null;
            }
            return cur;
        }
    }

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(10);
        treeNode.left = new TreeNode(5);
        treeNode.right = new TreeNode(9);
        treeNode.left.left = new TreeNode(3);
        treeNode.left.right = new TreeNode(8);
        treeNode.left.left.left = new TreeNode(2);
        treeNode.left.left.right = new TreeNode(4);
        treeNode.left.left.left.left = new TreeNode(1);
        treeNode.left.right.left = new TreeNode(7);
        treeNode.left.right.right = new TreeNode(9);
        treeNode.left.right.left.left = new TreeNode(6);
        T68 t68 = new T68();
//        TreeNode treeNode1 = t68.deleteNode(treeNode, 5);
//        System.out.println(treeNode1);

        TreeNode t = new TreeNode(5);
        t.left = new TreeNode(3);
        t.left.left = new TreeNode(2);
        t.right = new TreeNode(6);
        t.right.right = new TreeNode(7);
        TreeNode t2 = t68.deleteNode(t, 3);
        System.out.println(t2);


    }

}
