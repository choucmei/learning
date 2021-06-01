package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C65MinPathSum
 * @Package chouc.java.algorithm.leetcode
 * @Description: 给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 * 说明：每次只能向下或者向右移动一步。
 * <p>
 * 动态规划
 */
public class C64MinPathSum {
    public int minPathSum(int[][] grid) {
        int c = grid[0].length, r = grid.length;
        int[][] dp = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (i == 0 && j == 0) {
                    dp[j][i] = grid[j][i];
                    continue;
                }
                if (i == 0) {
                    dp[i][j] = dp[i][j - 1] + grid[i][j];
                } else if (j == 0) {
                    dp[i][j] = dp[i - 1][j] + grid[i][j];
                } else {
                    dp[i][j] = Math.min(dp[i][j - 1], dp[i - 1][j]) + grid[i][j];
                }
            }
        }
        return dp[r - 1][c - 1];
    }


    public static void main(String[] args) {
        C64MinPathSum c = new C64MinPathSum();
        int[][] a = new int[][]{{1, 3, 1}, {1, 5, 1}};
        int col = a[0].length, row = a.length;
        int[][] dp = new int[col][row];
        int cold = dp[0].length, rowd = dp.length;
        System.out.println("col:" + col + " row:" + row);
        System.out.println("col:" + cold + " row:" + rowd);
        System.out.println(c.minPathSum(a));
    }
}
