package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C26RemoveArrayDuplicates
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/4
 */
public class C27RemoveArrayDuplicates {
    public int removeDuplicates(int[] nums, int val) {
        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != val) {
                nums[i] = nums[j];
                i++;
            }
        }
        return i;
    }


    public static void main(String[] args) {
        int[] a = new int[]{0,1,2,2,3,0,4,2};
        C27RemoveArrayDuplicates c = new C27RemoveArrayDuplicates();
        int result = c.removeDuplicates(a,2);
        System.out.println(result);
        Utils.printArray(a);
    }
}
