package chouc.algorithm.leetcode_top;

public class T57 {
    public int maxProfit(int[] prices) {
        int in = prices[0], pre = prices[0], sum = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] >= pre) {
                pre = prices[i];
                continue;
            } else {
                sum += (pre - in);
                in = prices[i];
                pre = prices[i];
            }
        }
        if (pre > in){
            sum += (pre - in);
        }
        return sum;
    }

    public static void main(String[] args) {
        T57 t57 = new T57();
        System.out.println(t57.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
        System.out.println(t57.maxProfit(new int[]{1,2,3,4,5}));
        System.out.println(t57.maxProfit(new int[]{7,6,4,3,1}));
    }
}