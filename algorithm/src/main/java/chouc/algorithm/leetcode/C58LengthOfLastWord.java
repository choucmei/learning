package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C58LengthOfLastWord
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/3/4
 */
public class C58LengthOfLastWord {
    public int lengthOfLastWord(String s) {
        s = s.trim();
        int length = s.length();
        for (int i = length - 1; i >= 0; i--) {
            if (s.charAt(i) == ' ' && i != length - 1) {
                return length - i - 1;
            }
        }
        return s.length();
    }

    public static void main(String[] args) {
        C58LengthOfLastWord c = new C58LengthOfLastWord();
        System.out.println(c.lengthOfLastWord(" "));
    }
}
