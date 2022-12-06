package chouc.algorithm.leetcode;

import chouc.algorithm.common.Utils;

/**
 * @author chouc
 * @version V1.0
 * @Title: C48Rotate
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/22
 */
public class C48Rotate {
    public void rotate(int[][] matrix) {
        int[][] nMatrx = new int[matrix.length][matrix.length];
        int length = nMatrx.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                nMatrx[j][length - i - 1] = matrix[i][j];
            }
        }
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                matrix[i][j] = nMatrx[i][j];
            }
        }
    }


    public static void main(String[] args) {
        int[][] b = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Utils.printArrayInArray(b);
        C48Rotate c = new C48Rotate();
        c.rotate(b);
        Utils.printArrayInArray(b);


    }
}
