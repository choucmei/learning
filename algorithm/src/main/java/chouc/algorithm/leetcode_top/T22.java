package chouc.algorithm.leetcode_top;

import java.util.ArrayList;
import java.util.List;

public class T22 {
    int[][] mapping = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public List<Integer> spiralOrder(int[][] matrix) {
        int oLen = matrix.length, iLen = matrix[0].length, index = 0, i = 0, j = 0, nI = 0, nJ = 0, total = oLen * iLen;
        boolean[][] visit = new boolean[oLen][iLen];
        ArrayList<Integer> integers = new ArrayList<>();
        while (integers.size() < total) {
            visit[i][j] = true;
            integers.add(matrix[i][j]);
            int[] pos = mapping[index % 4];
            nI = i + pos[0];
            nJ = j + pos[1];
            if (nI >= oLen || nJ >= iLen || nI < 0 || nJ < 0 || visit[nI][nJ]) {
                index += 1;
                pos = mapping[index % 4];
                i = i + pos[0];
                j = j + pos[1];
            } else {
                i = nI;
                j = nJ;
            }
        }
        return integers;
    }

    public static void main(String[] args) {
        T22 t22 = new T22();
        int[][] ints = {{1}};
        System.out.println(t22.spiralOrder(ints));

//        {1,2,3,6,9,8,7,4,5}
    }
}
