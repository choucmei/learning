package chouc.algorithm.leetcode_top;

import chouc.algorithm.common.Utils;

public class T66 {
    public int candy(int[] ratings) {
        int length = ratings.length;
        int[] candy = new int[length];
        candy[0] = 1;
        for (int i = 1; i < length; i++) {
            if (ratings[i - 1] < ratings[i]) {
                candy[i] = candy[i - 1] + 1;
            } else {
                candy[i] = 1;
            }
        }
        Utils.printArray(candy);
        int count = candy[length - 1];
        for (int i = length - 2; i >= 0; i--) {
            if (ratings[i + 1] < ratings[i] && candy[i] <= candy[i + 1]) {
                candy[i] = candy[i + 1] + 1;
            }
            count += candy[i];
        }
        Utils.printArray(candy);
        return count;
    }

    public static void main(String[] args) {
        T66 t66 = new T66();
        System.out.println(t66.candy(new int[]{1, 0, 2}));
        System.out.println(t66.candy(new int[]{1, 2, 2}));
        System.out.println(t66.candy(new int[]{1, 3, 4, 5, 2}));

//        System.out.println(t66.candy(new int[]{1, 0, 2}));
    }
}
