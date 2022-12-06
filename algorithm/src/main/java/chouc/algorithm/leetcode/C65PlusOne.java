package chouc.algorithm.leetcode;

import chouc.algorithm.common.Utils;

/**
 * @author chouc
 * @version V1.0
 * @Title: C65PlusOne
 * @Package chouc.java.algorithm
 * @Description:
 * @date 2021/4/2
 */
public class C65PlusOne {
    public int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            digits[i]++;
            digits[i] = digits[i] % 10;
            if (digits[i] != 0) {
                return digits;
            }
        }
        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;
    }

    public static void main(String[] args) {
        int[] a = new int[]{4, 3, 2, 1};
        C65PlusOne c = new C65PlusOne();
        Utils.printArray(c.plusOne(a));
    }
}
