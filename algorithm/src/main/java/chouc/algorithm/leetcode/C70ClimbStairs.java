package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C70ClimbStairs
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/3/11
 */
public class C70ClimbStairs {
    public int climbStairs(int n) {
        int pre = 0, cur = 0, next = 1;
        for (int i = 1; i <= n; i++) {
            pre = cur;
            cur = next;
            next = pre + cur;
        }
        return next;
    }

    public static void main(String[] args) {
        C70ClimbStairs c = new C70ClimbStairs();
        System.out.println(c.climbStairs(44));
    }
}
