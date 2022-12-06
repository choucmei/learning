package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

import java.util.HashMap;
import java.util.Map;

public class T83 {
//    输入：inorder = [9,3,15,20,7], postorder = [9,15,7,20,3]
//    输出：[3,9,20,null,null,15,7]

    public TreeNode buildTree(int[] inorder, int[] postorder) {
        Map<Integer, Integer> mapping = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            mapping.put(inorder[i], i);
        }
        return buildTree(postorder, mapping, 0, 0, postorder.length - 1);
    }

    public TreeNode buildTree(int[] postorder, Map<Integer, Integer> mapping, int inStart, int pStart, int pEnd) {
        if (pStart > pEnd) {
            return null;
        }
        int curVal = postorder[pEnd];
        TreeNode treeNode = new TreeNode(curVal);
        Integer index = mapping.get(curVal);
        treeNode.left = buildTree(postorder, mapping, inStart, pStart, pStart + (index - inStart) - 1);
        treeNode.right = buildTree(postorder, mapping, index + 1, pStart + (index - inStart), pEnd - 1);
        return treeNode;
    }


    public static void main(String[] args) {
        int[] in = {9, 3, 15, 20, 7};
        int[] post = {9, 15, 7, 20, 3};
        T83 t83 = new T83();
        System.out.println(t83.buildTree(in, post));
    }
}
