package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C6ZConvert
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/11/30
 */
public class C6ZConvert {
    public static String convert(String s, int numRows) {
        StringBuilder sb = new StringBuilder();
        int length = s.length();
        int cycleLen = numRows + ((numRows - 2) > 0 ? numRows - 2 : 0);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j + i < length; j += cycleLen) {
                sb.append(s.charAt(j + i));
                if (i != 0 && i != numRows - 1 && (j + cycleLen - i) < length) {
                    sb.append(s.charAt((j + cycleLen - i)));
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String a = "LEETCODEISHIRING";
        System.out.println(convert(a, 4));
    }
}
