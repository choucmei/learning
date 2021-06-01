package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C11
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/6
 */
public class C11MaxArea {
    public static int maxArea(int[] height) {
        int max = 0;
        int i = 0, j = height.length - 1;
        while (i < j) {
            int h = Math.min(height[i], height[j]);
            int l = j - i;
            int t = h * l;
            if (t > max) {
                max = t;
            }
            if (height[i] > height[j]) {
                j--;
            }else {
                i++;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int[] h = new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println(maxArea(h));
    }
}
