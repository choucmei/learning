package chouc.algorithm.leetcode_top;

public class T65 {
    public int maxArea(int[] height) {
        int i = 0, j = height.length - 1, max = 0;
        while (i < j) {
            max = Math.max((j - i) * (Math.min(height[i], height[j])), max);
            if (height[i] > height[j]) {
                j--;
            } else {
                i++;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        T65 t65 = new T65();
        System.out.println(t65.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
    }
}
