package chouc.algorithm.leetcode_top;

public class T12 {
    public int maxProfit(int[] prices) {
        int maxProfit = 0, in = prices[0];
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > in) {
                maxProfit = Math.max(maxProfit, (prices[i] - in));
            } else {
                in = prices[i];
            }
        }
        return maxProfit;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{7, 1, 5, 3, 6, 4};
        T12 t12 = new T12();
        System.out.println(t12.maxProfit(arr));
    }
}
