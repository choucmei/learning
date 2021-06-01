package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C73SetZeroes
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/4/8
 */
public class C73SetZeroes {
    public void setZeroes(int[][] matrix) {
        int row = matrix.length, col = matrix[0].length;
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 0) {
                    list.add(new int[]{i, j});
                }
            }
        }
        for (int[] index : list) {
            int r = index[0], c = index[1];
            int tmp = r;
            while (tmp > 0) {
                matrix[tmp - 1][c] = 0;
                tmp--;
            }
            tmp = r;
            while (tmp < row - 1) {
                matrix[tmp + 1][c] = 0;
                tmp++;
            }

            tmp = c;
            while (tmp > 0) {
                matrix[r][tmp - 1] = 0;
                tmp--;
            }

            tmp = c;
            while (tmp < col - 1) {
                matrix[r][tmp + 1] = 0;
                tmp++;
            }
        }

    }

    public static void main(String[] args) {
        int[][] arr = new int[][]{{1, 2, 3, 4}, {5, 0, 7, 8}, {0, 10, 11, 12}, {13, 14, 15, 0}};
        Utils.printArrayInArray(arr);
        System.out.println();
        C73SetZeroes c = new C73SetZeroes();
        c.setZeroes(arr);
        Utils.printArrayInArray(arr);
    }
}
