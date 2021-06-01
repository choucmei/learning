package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C26RemoveArrayDuplicates
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/4
 */
public class C26RemoveArrayDuplicates {
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;

    }


    public static void main(String[] args) {
        int[] a = new int[]{0,0,1,1,1,2,2,3,3,4};
        C26RemoveArrayDuplicates c = new C26RemoveArrayDuplicates();
        int result = c.removeDuplicates(a);
        System.out.println(result);
        Utils.printArray(a);
    }
}
