package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C33
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/12
 */
public class C33Search {
    public int search(int[] nums, int target) {
        int length = nums.length;
        if (length == 0) {
            return -1;
        }
        if (length == 1) {
            return nums[0] == target ? 0 : -1;
        }
        int start = 0;
        int end = length - 1;
        while (start <= end) {
            if (start==end){
                return nums[start]==target?start:-1;
            }
            int mid = (end + start) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            System.out.println("start:" + start + " end:" + end+" mid:"+mid);
            if (nums[start] <= nums[mid]) {
                if (nums[mid] >= target && nums[start] <= target) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else {
                if (nums[mid] <= target && nums[end] >= target) {
                    start = mid+1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        C33Search c = new C33Search();
        int[] a = new int[]{5,1,3};
        System.out.println(c.search(a, 5));
    }
}
