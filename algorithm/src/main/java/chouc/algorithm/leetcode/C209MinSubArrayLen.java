package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C209MinSubArrayLen
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/4/6
 */
public class C209MinSubArrayLen {
    public int minSubArrayLen(int target, int[] nums) {
        int start = 0, end = 0, sum = 0, length = nums.length, min = length + 1;
        while (length >= end) {
            System.out.println("sum:" + sum + " start:" + start + " end:" + end + " min:" + min);
            if (sum < target) {
                if (end >= length) {
                    break;
                }
                sum += nums[end];
                end++;
            } else {
                sum -= nums[start];
                min = Math.min(min, (end - start));
                start++;
            }
        }
        return min>length?0:min;
    }


    public int minSubArrayLen2(int target, int[] nums) {
        int start = 0, end = 0, sum = 0, length = nums.length, min = length + 1;
        while (length > end) {
            sum += nums[end];
            while (sum >= target) {
                System.out.println("sum:" + sum + " start:" + start + " end:" + end + " min:" + min);
                min = Math.min(min, (end - start+1));
                sum -= nums[start];
                start++;
            }
            end++;
        }
        return min>length?0:min;
    }


    public static void main(String[] args) {
        int[] a = new int[]{1, 2, 3, 4, 5};
        C209MinSubArrayLen c = new C209MinSubArrayLen();
        System.out.println(c.minSubArrayLen2(11, a));
    }
}
