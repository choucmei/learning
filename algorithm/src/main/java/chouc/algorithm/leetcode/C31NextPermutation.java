package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C31NextPermutation
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/10
 */
public class C31NextPermutation {


    public void nextPermutation(int[] nums) {
        if (nums.length <= 1) {
            return;
        }
        int i = nums.length - 2;
        while (i >= 1 && nums[i] > nums[i+1]) {
            i--;
        }
        if (i > 0) {
            int j = nums.length - 1;
            while (j >= 0 && nums[i] >= nums[j]) {
                j--;
            }
            System.out.println("i"+(i));
            System.out.println("j"+j);
            swap(nums, i, j);
        }
        reverses(nums, i);
    }

    private void reverses(int[] array, int i) {
        int j = array.length - 1;
        while (i < j) {
            swap(array, i, j);
            i++;
            j--;
        }
    }

    private void swap(int[] array, int a, int b) {
        System.out.println(a);
        System.out.println(b);
        int tmp = array[a];
        array[a] = array[b];
        array[b] = tmp;
    }

    public static void main(String[] args) {
        C31NextPermutation c = new C31NextPermutation();
        int[] a = new int[]{1, 1};
        c.nextPermutation(a);
        Utils.printArray(a);
    }


}
