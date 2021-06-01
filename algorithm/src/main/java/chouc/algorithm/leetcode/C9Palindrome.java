package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C9Palindrome
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/4
 */
public class C9Palindrome {

    static boolean isPalindrome(int i) {
        if (i < 0 || (i % 10 == 0 && i != 0)) {
            return false;
        }
        int result = 0;
        int pop = 0;
        while (i > result) {
            pop = i % 10;
            i = i / 10;
            result = result * 10 + pop;
        }
        return i == result || result / 10 == i;
    }

    public static void main(String[] args) {
        System.out.println(isPalindrome(10));
    }


}
