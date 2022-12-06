package chouc.algorithm.leetcode_top;

public class T71 {
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        int start = 0, end = matrix[0].length - 1;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][start] <= target && matrix[i][end] >= target) {
                int s = start, e = end, mid;
                while (s <= e) {
                    mid = (s + e) / 2;
                    if (matrix[i][mid] > target) {
                        e = mid - 1;
                    } else if (matrix[i][mid] < target) {
                        s = mid + 1;
                    } else {
                        return true;
                    }
                }
            }
            if (matrix[i][start] > target) {
                break;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        T71 t71 = new T71();
        System.out.println(t71.findNumberIn2DArray(new int[][]{
                {1, 4, 7, 11, 15},
                {2, 5, 8, 12, 19},
                {3, 6, 9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}
        }, 5));
        System.out.println(t71.findNumberIn2DArray(new int[][]{
                {1, 4, 7, 11, 15},
                {2, 5, 8, 12, 19},
                {3, 6, 9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}
        }, 20));
    }
}
