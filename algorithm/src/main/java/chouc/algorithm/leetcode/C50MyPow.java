package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C50MyPow
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/22
 */
public class C50MyPow {
    public double myPow(double x, int n) {
        if (n==0){
            return 1;
        }
        long nc = n;
        return n >= 0 ? pow(x, nc) : 1 / pow(x, -nc);
    }

    public double pow(double x, long n) {
        if (n == 1) {
            return n;
        }
        double r = pow(x, n / 2);
        return n % 2 == 1 ? r * r * x : r * r;
    }

    public static void main(String[] args) {
        C50MyPow c = new C50MyPow();
        System.out.println(c.myPow(2.0d, -10));
    }


}
