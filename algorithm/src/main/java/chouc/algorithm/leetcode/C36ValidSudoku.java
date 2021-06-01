package chouc.algorithm.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chouc
 * @version V1.0
 * @Title: C36ValidSudoku
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/23
 */
public class C36ValidSudoku {
    public boolean isValidSudoku(char[][] board) {
        Map<Integer, Integer>[] rows = new HashMap[9];
        Map<Integer, Integer>[] cols = new HashMap[9];
        Map<Integer, Integer>[] boxs = new HashMap[9];
        for (int i = 0; i < 9; i++) {
            rows[i] = new HashMap<Integer, Integer>();
            cols[i] = new HashMap<Integer, Integer>();
            boxs[i] = new HashMap<Integer, Integer>();
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                char c = board[i][j];
                if (c != '.') {
                    int n = (int) c;
                    rows[i].put(n, 1 + rows[i].getOrDefault(n, 0));
                    cols[j].put(n, 1 + cols[j].getOrDefault(n, 0));
                    int boxIndex = (i / 3) * 3 + j / 3;
                    boxs[boxIndex].put(n, 1 + boxs[boxIndex].getOrDefault(n, 0));
                    if (rows[i].get(n) > 1 || cols[j].get(n) > 1 || boxs[boxIndex].get(n) > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
