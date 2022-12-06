package chouc.algorithm.leetcode_top;

import java.util.Arrays;
import java.util.List;

public class T79 {
    public int minimumTotal(List<List<Integer>> triangle) {
        int size = triangle.size();
        int[][] dp = new int[size][size];
        dp[0][0] = triangle.get(0).get(0);
        for (int i = 1; i < size; i++) {
            List<Integer> cur = triangle.get(i);
            for (int j = 0; j <= i; j++) {
                int preMin = j - 1 >= 0 ? dp[i - 1][j - 1] : Integer.MAX_VALUE;
                preMin = j <= i - 1 ? Math.min(dp[i - 1][j], preMin) : preMin;
                dp[i][j] = preMin + cur.get(j);
            }
        }
        int min = dp[size - 1][0];
        for (int i : dp[size - 1]) {
            min = Math.min(min, i);
        }
        return min;
    }


    public static void main(String[] args) {
//        [[2],[3,4],[6,5,7],[4,1,8,3]]
        List<List<Integer>> lists = Arrays.asList(Arrays.asList(2), Arrays.asList(3, 4), Arrays.asList(6, 5, 7), Arrays.asList(4, 1, 8, 3));
        T79 t79 = new T79();
        System.out.println(t79.minimumTotal(lists));
    }


}
