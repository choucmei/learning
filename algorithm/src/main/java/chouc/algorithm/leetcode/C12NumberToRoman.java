package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C12NumberToRoman
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/7
 */
public class C12NumberToRoman {
    int nums[] = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    String strings[] = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public  String intToRoman(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nums.length && num > 0; i++) {
            System.out.println(num);
            while (nums[i] <= num) {
                num = num-nums[i];
                sb.append(strings[i]);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        C12NumberToRoman c = new C12NumberToRoman();
        String r = c.intToRoman(1994);
        System.out.println(r);
        System.out.println(r.equals("MCMXCIV"));
    }
}
