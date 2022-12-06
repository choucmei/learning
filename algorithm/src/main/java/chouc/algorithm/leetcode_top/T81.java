package chouc.algorithm.leetcode_top;

public class T81 {
    public int jump(int[] nums) {
        int step = 0, index = 0, length = nums.length, lastIndex = nums.length - 1;
        while (index < lastIndex) {
            step++;
            if (index + nums[index] >= lastIndex) {
                break;
            }
            int maxIndex = 0, nextIndex = 0;
            for (int i = 1; i <= nums[index]; i++) {
                if (index + i + nums[index + i] > maxIndex) {
                    maxIndex = index + i + nums[index + i];
                    nextIndex = index + i;
                }
            }
            index = nextIndex;
        }
        return step;
    }


    public static void main(String[] args) {
        T81 t81 = new T81();
//        System.out.println(t81.jump(new int[]{2, 3, 1, 1, 4}));
//        System.out.println(t81.jump(new int[]{2, 3, 0, 1, 4}));
//        System.out.println(t81.jump(new int[]{0}));
        System.out.println(t81.jump(new int[]{1, 2, 3}));
    }
}
