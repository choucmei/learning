package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C39CombinationSum
 * @Package chouc.java.algorithm.leetcode
 * @Description: 困难
 * @date 2021/1/29
 */
public class C41FirstMissingPositive {
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] <= 0) {
                nums[i] = n + 1;
            }
        }
        for (int i = 0; i < n; i++) {
            int num = Math.abs(nums[i]);
            if (num <= n) {
                nums[num - 1] = -Math.abs(nums[num - 1]);
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] > 0) {
                return i + 1;
            }
        }
        return n + 1;
    }

    public int firstMissingPositive2(int[] nums) {
        int n = nums.length;
        Utils.printArray(nums);
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            System.out.println(num+" i:"+i+" n:"+n);
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                int temp = nums[nums[i] - 1];
                nums[nums[i] - 1] = nums[i];
                nums[i] = temp;
                Utils.printArray(nums);

            }
        }
        Utils.printArray(nums);

        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return n + 1;
    }


    public static void main(String[] args) {
        C41FirstMissingPositive c = new C41FirstMissingPositive();
        System.out.println(c.firstMissingPositive2(new int[]{3,4,-1,1}));
        ;
    }

}
