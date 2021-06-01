package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C38CountAndSay
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/28
 */
public class C38CountAndSay {
    public String countAndSay(int n) {
        if (n==1){
            return String.valueOf(n);
        }
        return countAndSay(n, new StringBuilder("1")).toString();
    }

    public StringBuilder countAndSay(int n, StringBuilder value) {
        StringBuilder sb = new StringBuilder();
        char p = value.charAt(0);
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == p) {
                count = count + 1;
            } else {
                sb.append(count).append(p);
                p = c;
                count = 1;
            }
        }
        if (count > 0) {
            sb.append(count).append(p);
        }
        if (n - 1 == 1) {
            return sb;
        } else {
            return countAndSay(n - 1, sb);
        }
    }

    public static void main(String[] args) {
        C38CountAndSay c = new C38CountAndSay();

    }
}
