package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C121Stock
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/4/12
 */
public class C121Stock {
    public int max(int[] arr) {
        int in = arr[0];
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] <= in) {
                in = arr[i];
            } else {
                max = Math.max(max, arr[i] - in);
            }
        }
        return max;
    }

    public static void main(String[] args) {
        C121Stock c = new C121Stock();
        System.out.println(c.max(new int[]{7, 1, 5, 3, 6, 4}));
    }
}
