package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.TreeNode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class T11LevelOrder {
    public List<List<Integer>> levelOrder(TreeNode root) {
        Deque<TreeNode> deque = new LinkedList();
        List<List<Integer>> result = new ArrayList<>();
        if (root != null) {
            deque.add(root);
        }
        while (deque.size() != 0) {
            int size = deque.size();
            List<Integer> inner = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                TreeNode poll = deque.poll();
                inner.add(poll.val);
                if (poll.left != null) {
                    deque.add(poll.left);
                }
                if (poll.right != null) {
                    deque.add(poll.right);
                }
            }
            result.add(inner);
        }
        return result;
    }
}
