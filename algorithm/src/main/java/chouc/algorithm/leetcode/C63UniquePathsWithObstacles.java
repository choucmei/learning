package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C63UniquePathsWithObstacles
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。
 * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。
 * 现在考虑网格中有障碍物。那么从左上角到右下角将会有多少条不同的路径？
 * 动态规划
 * @date 2021/4/2
 */
public class C63UniquePathsWithObstacles {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if (obstacleGrid.length == 0) {
            return 0;
        }
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = Math.abs(obstacleGrid[0][0] - 1);
        for (int i = 1; i < m; i++) {
            dp[i][0] = obstacleGrid[i][0] == 1 ? 0 : dp[i - 1][0];
        }
        for (int i = 1; i < n; i++) {
            dp[0][i] = obstacleGrid[0][i] == 1 ? 0 : dp[0][i - 1];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    continue;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        Utils.printArrayInArray(dp);
        return dp[m - 1][n - 1];
    }


    public static void main(String[] args) {
        int[][] d = new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        C63UniquePathsWithObstacles c63UniquePathsWithObstacles = new C63UniquePathsWithObstacles();
        System.out.println(c63UniquePathsWithObstacles.uniquePathsWithObstacles(d));
    }
}
