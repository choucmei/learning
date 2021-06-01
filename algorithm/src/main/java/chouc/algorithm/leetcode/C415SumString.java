package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C415SumString
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/3
 */
public class C415SumString {
    public String addStrings(String num1, String num2) {
        int i = num1.length() - 1, j = num2.length() - 1, add = 0;
        StringBuilder sb = new StringBuilder();
        while (i >= 0 || j >= 0 || add != 0) {
            int x = i >= 0 ? num1.charAt(i)-'0' : 0;
            int y = j >= 0 ? num2.charAt(j)-'0' : 0;
            System.out.println("x:"+x+" y:"+y+" a:"+add);
            int result = x + y + add;
            sb.append(result % 10);
            add = result / 10;
            i--;
            j--;
        }
        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        C415SumString c= new C415SumString();
        System.out.println(c.addStrings("999","999"));
    }
}
