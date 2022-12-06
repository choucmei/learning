package chouc.algorithm.leetcode_top;

public class T41 {
    public int myAtoi(String s) {
        int i = 0, sign = 1;
        Long value = 0l;
        for (; i < s.toCharArray().length; i++) {
            if (s.charAt(i) == ' ') {
                continue;
            } else {
                break;
            }
        }
        if (i < s.length() && (s.charAt(i) == '-' || s.charAt(i) == '+')) {
            sign = s.charAt(i) == '-' ? -1 : 1;
            i++;
        }
        for (; i < s.toCharArray().length; i++) {
            int t = s.charAt(i) - '0';
            if (t <= 9 && t >= 0) {
                value = value * 10 + t;
                long l = value * sign;
                if (l > Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                } else if (l < Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }
            } else {
                break;
            }
        }
        return (int) (value * sign);
    }

    public static void main(String[] args) {
        T41 t41 = new T41();
        System.out.println(t41.myAtoi(""));
        System.out.println(t41.myAtoi("42"));
        System.out.println(t41.myAtoi("   -42"));
        System.out.println(t41.myAtoi("4193 with words"));
        System.out.println(t41.myAtoi("-91283472332"));
        System.out.println(t41.myAtoi("9223372036854775808"));

    }
}
