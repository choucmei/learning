package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C74SearchMatrix
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/4/8
 */
public class C74SearchMatrix {
    public boolean searchMatrix(int[][] matrix, int target) {
        int colLength = matrix[0].length;
        int startRow = 0, startCol = 0, endRow = matrix.length - 1, endCol = colLength - 1;
        while (startRow <= endRow) {
            int middleRow = (endRow + startRow) / 2;
            if (matrix[middleRow][0] > target) {
                endRow = middleRow - 1;
            } else if (matrix[middleRow][colLength - 1] < target) {
                startRow = middleRow + 1;
            } else {
                while (endCol >= startCol) {
                    int middleCol = (endCol + startCol) / 2;
                    if (matrix[middleRow][middleCol] > target) {
                        endCol = middleCol - 1;
                    } else if (matrix[middleRow][middleCol] < target) {
                        startCol = middleCol + 1;
                    } else {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

//    private boolean searchMatrix(int[][] matrix, int target,int start,end) {
//        int row = matrix.length,col = matrix[0].length;
//    }

    public static void main(String[] args) {
        int[][] a = new int[][]{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        C74SearchMatrix c = new C74SearchMatrix();
        System.out.println(c.searchMatrix(a, 3));
    }
}
