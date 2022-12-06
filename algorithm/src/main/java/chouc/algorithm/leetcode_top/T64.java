package chouc.algorithm.leetcode_top;

public class T64 {
    public int findKthNumber(int n, int k) {
        int cur = 1;
                while (k > 0) {
            long cnt = dfs(cur, cur, n);
            if (cnt > k) {
                cur *= 10;
                k--;
            } else {
                cur += 1;
                k -= cnt;
            }
        }
        return cur;
    }

    private long dfs(long l, long r, int n) {
        if (l > n)
            return 0;
        return Math.min(r, n) - l + 1 + dfs(l * 10, r * 10 + 9, n);
    }

    public static void main(String[] args) {
        T64 t64 = new T64();
        System.out.println(t64.findKthNumber(13, 3));
    }

}
