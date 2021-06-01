package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C28StrStr
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/4
 */
public class C28StrStr {
    public int strStr(String haystack, String needle) {
        for (int i = 0; i < haystack.length() - needle.length() + 1; i++) {
            if (haystack.substring(i,i+needle.length()).equals(needle)){
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        C28StrStr c = new C28StrStr();
        System.out.println(c.strStr("hello", "ll"));
    }
}
