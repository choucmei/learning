package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: Main
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/29
 */
public class Main {
    public String stringCompact(String str) {
        if (str.length() == 0) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        char pre = str.charAt(0);
        byte count = 0;
        for (char c : str.toLowerCase().toCharArray()) {
            if (pre == c) {
                count += 1;
            } else {
                sb.append(pre);
                if (count > 1) {
                    sb.append(count);
                }
                count = 1;
                pre = c;
            }
        }
        if (count > 0) {
            sb.append(pre);
            if (count > 1) {
                sb.append(count);
            }
        }
        return sb.length()>=str.length()?str:sb.toString();
    }

    public static void main(String[] args) {
        Main m = new Main();
        System.out.println(m.stringCompact("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }
}
