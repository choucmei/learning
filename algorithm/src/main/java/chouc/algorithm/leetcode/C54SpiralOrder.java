package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C54SpiralOrder
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/25
 */
public class C54SpiralOrder {
    public List<Integer> spiralOrder(int[][] matrix) {
        int rowLength = matrix[0].length;
        int colLength = matrix.length;
        int[][] dict = {{1, 0}, {0, 1}, {-1, 0}, {0,-1}};
        boolean[][] visited = new boolean[rowLength][colLength];
        int row = 0, col = 0, preRow = 0, preCol = 0, dictIndex = 0;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < rowLength * colLength; i++) {
            System.out.println(row+"-"+col);
            list.add(matrix[col][row]);
            visited[row][col] = true;
            preRow = row + dict[dictIndex][0];
            preCol = col + dict[dictIndex][1];
            if (preRow < 0 || preRow >= rowLength || preCol < 0 || preCol >= colLength || visited[preRow][preCol]) {
                dictIndex = (dictIndex + 1) % 4;
                System.out.println("reverse");
            }
            row = row + dict[dictIndex][0];
            col = col + dict[dictIndex][1];
        }
        return list;
    }

    public static void main(String[] args) {
        C54SpiralOrder c = new C54SpiralOrder();
        System.out.println(c.spiralOrder(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));
    }
}
