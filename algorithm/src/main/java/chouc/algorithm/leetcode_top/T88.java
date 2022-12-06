package chouc.algorithm.leetcode_top;

public class T88 {
    public int fib(int n) {
        if (n <= 1) {
            return n;
        }
        int pre = 0, cur = 1, tmp = 0;
        for (int i = 1; i < n; i++) {
            tmp = cur + pre;
            pre = cur;
            cur = tmp;
        }
        return cur;
    }
}
