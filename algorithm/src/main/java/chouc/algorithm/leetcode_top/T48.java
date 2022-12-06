package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

import java.util.HashMap;
import java.util.Map;

public class T48 {
    Map<Integer, Integer> map = new HashMap<>();

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return buildTree(preorder, 0, 0, preorder.length - 1);
    }

    public TreeNode buildTree(int[] preorder, int preS, int inS, int inE) {
        int nodeValue = preorder[preS];
        int nodeIndexInorder = map.get(nodeValue);
        int leftSize = nodeIndexInorder - inS;
        int rightSize = inE - nodeIndexInorder;
        TreeNode treeNode = new TreeNode(nodeValue);
        if (leftSize > 0) {
            treeNode.left = buildTree(preorder, preS + 1, inS, nodeIndexInorder - 1);
        }
        if (rightSize > 0) {
            treeNode.right = buildTree(preorder, preS + leftSize + 1, nodeIndexInorder + 1, inE);
        }
        return treeNode;
    }

    public static void main(String[] args) {
        int[] preOrder = new int[]{3, 5, 6, 2, 7, 4, 1, 0, 8};
        int[] inOrder = new int[]{6, 5, 7, 2, 4, 3, 0, 1, 8};
        T48 t48 = new T48();
        TreeNode treeNode = t48.buildTree(preOrder, inOrder);
        treeNode.inorder(treeNode);
        System.out.println();
        treeNode.preorder(treeNode);
    }
}
