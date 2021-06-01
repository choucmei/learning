package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C509Fib
 * @Package chouc.algorithm.leetcode
 * @Description:
 * @date 2021/5/17
 */
public class C509Fib {
    public int fib(int n) {
        if (n < 2) {
            return n;
        }
        int p = 0, q = 0, r = 1;
        for (int i = 2; i <= n; i++) {
            p = q;
            q = r;
            r  = p + q;
        }
        return r;
    }


    public static void main(String[] args) {
        C509Fib c = new C509Fib();
        System.out.println(c.fib(2));
    }
}
