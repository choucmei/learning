package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C45Multiply
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/4
 */
public class C43Multiply {
    final String ZERO = "0";
    public String multiply(String num1, String num2) {
        if (num1.equals(ZERO)||num2.equals(ZERO)){
            return ZERO;
        }
        String result = ZERO;
        for (int i = num1.length() - 1; i >= 0; i--) {
            StringBuffer sb = new StringBuffer();
            int add = 0;
            for (int j = i; j < num1.length() - 1; j++) {
                sb.append(ZERO);
            }
            for (int j = num2.length() - 1; j >= 0; j--) {
                int x = num1.charAt(i) - '0';
                int y = num2.charAt(j) - '0';
                int rs = x * y + add;
                add = rs / 10;
                sb.append(rs % 10);
            }
            if (add > 0) {
                sb.append(add);
            }

            String currentRs = sb.reverse().toString();
            System.out.println(currentRs);
            result = addStrings(result, currentRs);
        }
        return result;
    }

    public String addStrings(String num1, String num2) {
        int i = num1.length() - 1, j = num2.length() - 1, add = 0;
        StringBuilder sb = new StringBuilder();
        while (i >= 0 || j >= 0 || add != 0) {
            int x = i >= 0 ? num1.charAt(i) - '0' : 0;
            int y = j >= 0 ? num2.charAt(j) - '0' : 0;
            System.out.println("x:" + x + " y:" + y + " a:" + add);
            int result = x + y + add;
            sb.append(result % 10);
            add = result / 10;
            i--;
            j--;
        }
        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        C43Multiply c = new C43Multiply();
        System.out.println(c.multiply("999", "999"));
    }
}
