package chouc.algorithm.common;

public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "TreeNode{" + "val=" + val + ", left=" + left + ", right=" + right + '}';
    }

    public void preorder(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        System.out.print(treeNode.val + ", ");
        preorder(treeNode.left);
        preorder(treeNode.right);
    }

    public void inorder(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        inorder(treeNode.left);
        System.out.print(treeNode.val + ", ");
        inorder(treeNode.right);
    }

    public void eorder(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        eorder(treeNode.left);
        eorder(treeNode.right);
        System.out.print(treeNode.val + ", ");
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

        root.preorder(root);
        System.out.println(" ");
        root.inorder(root);
        System.out.println(" ");
        root.eorder(root);
    }
}