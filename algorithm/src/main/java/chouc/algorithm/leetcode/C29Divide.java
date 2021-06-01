package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C29Divide
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/4
 */
public class C29Divide {
    public int divide(int dividend, int divisor) {
        if (divisor == 0) {
            return 0;
        }
        boolean isNagative = (dividend > 0) ^ (divisor > 0);
        dividend = dividend > 0 ? -dividend : dividend;
        divisor = divisor > 0 ? -divisor : divisor;
        int result = 0;

        while (dividend <= divisor) {
            int temp_ji = 1;
            int temp_divisor = divisor;
            while (dividend <= temp_divisor << 1){
                if (temp_divisor <= (Integer.MIN_VALUE >> 1)){
                    System.out.println("break");
                    break;
                }
                System.out.println("t "+temp_divisor);
                temp_divisor = temp_divisor << 1;
                temp_ji = temp_ji << 1;
            }
            result += temp_ji;
            System.out.println(dividend);
            System.out.println(temp_divisor);
            dividend -= temp_divisor;
//            System.out.println(dividend);
        }
        if (isNagative) {
            result = -result;
        } else {
            if (result <= Integer.MIN_VALUE) return Integer.MAX_VALUE;
        }
        return result;
    }

    public static void main(String[] args) {
        C29Divide c = new C29Divide();
        System.out.println(c.divide(2147483647, 3));
        System.out.println(Integer.MIN_VALUE >> 1);
//        System.out.println(-2 - (-1));
    }
}
