package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C62UniquePaths
 * @Package chouc.java.algorithm.leetcode
 * @Description: 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为 “Start” ）。
 * <p>
 * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为 “Finish” ）。
 * <p>
 * 问总共有多少条不同的路径？
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/unique-paths
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * <p>
 * 动态规划
 * @date 2021/3/11
 */
public class C62UniquePaths {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (r == 0 || c == 0) {
                    dp[r][c] = 1;
                } else {
                    dp[r][c] = dp[r-1][c] + dp[r][c-1];
                }
            }
        }
        return dp[m-1][n-1];
    }

    public static void main(String[] args) {
        C62UniquePaths c = new C62UniquePaths();
        System.out.println(c.uniquePaths(3, 7));
    }
}
