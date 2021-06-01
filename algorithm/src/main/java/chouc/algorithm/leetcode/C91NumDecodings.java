package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C91NumDecodings
 * @Package chouc.algorithm.leetcode
 * @Description: 一条包含字母 A-Z 的消息通过以下映射进行了 编码 ：
 * <p>
 * 要 解码 已编码的消息，所有数字必须基于上述映射的方法，反向映射回字母（可能有多种方法）。例如，"11106" 可以映射为：
 * <p>
 * "AAJF" ，将消息分组为 (1 1 10 6)
 * "KJF" ，将消息分组为 (11 10 6)
 * 注意，消息不能分组为  (1 11 06) ，因为 "06" 不能映射为 "F" ，这是由于 "6" 和 "06" 在映射中并不等价。
 * <p>
 * 给你一个只含数字的 非空 字符串 s ，请计算并返回 解码 方法的 总数 。
 * <p>
 * 题目数据保证答案肯定是一个 32 位 的整数。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/decode-ways
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @date 2021/5/17
 */
public class C91NumDecodings {
    public int numDecodings(String s) {
        int length = s.length();
        int ppre = 0, pre = 1, current = 0;
        for (int i = 1; i <= length; i++) {
            current = 0;
            if (s.charAt(i - 1) != '0') {
                current += pre;
            }
            if (i > 1 && s.charAt(i - 2) != '0' && ((s.charAt(i - 2) - '0') * 10 + (s.charAt(i - 1) - '0') <= 26)) {
                current += ppre;
            }
            ppre = pre;
            pre = current;
        }
        return current;
    }

    public static void main(String[] args) {
        C91NumDecodings c = new C91NumDecodings();
        System.out.println('2' < '3');
        System.out.println(c.numDecodings("123"));
    }
}
