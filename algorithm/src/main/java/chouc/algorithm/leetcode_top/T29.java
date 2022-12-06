package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class T29 {
    List<Integer> list;
    public List<Integer> inorderTraversal(TreeNode root) {
        list = new ArrayList<Integer>();
        dfs(root);
        return list;
    }

    public void dfs(TreeNode root) {
        if (root == null){
            return;
        }
        list.add(root.val);
        dfs(root.left);
        dfs(root.right);
    }


}
