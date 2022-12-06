package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.Utils;

public class T80 {
    int[][] mapping = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public int[] spiralOrder(int[][] matrix) {
        if (matrix.length == 0) {
            return new int[]{};
        }
        int len1 = matrix.length, len2 = matrix[0].length, i = 0, j = 0, cur = 0, count = len1 * len2, index = 0;
        boolean[][] visited = new boolean[len1][len2];
        int[] ret = new int[count];
        while (cur < count) {
            ret[cur++] = matrix[i][j];
            visited[i][j] = true;
            int nI = mapping[index][0] + i, nJ = mapping[index][1] + j;
            if (nI >= 0 && nI < len1 && nJ >= 0 && nJ < len2 && !visited[nI][nJ]) {
                i = nI;
                j = nJ;
            } else {
                index = (index + 1) % 4;
                i = mapping[index][0] + i;
                j = mapping[index][1] + j;
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        T80 t80 = new T80();
        Utils.printArray(t80.spiralOrder(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));
    }
}
