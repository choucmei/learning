package chouc.algorithm.leetcode_top;

public class T59 {
    public int findMin(int[] nums) {
        int s = 0, e = nums.length - 1;
        while (s < e && nums[s] > nums[e]) {
            int mid = (s + e) / 2;
            if (nums[s] <= nums[mid]) {
                s = mid + 1;
            } else {
                e = mid;
            }
        }
        return nums[s];
    }


    public int findM(int[] nums, int t) {
        int s = 0, e = nums.length - 1;
        while (s < e) {
            int mid = (s + e) / 2;
            if (nums[mid] == t) {
                return 1;
            } else if (nums[mid] > t) {
                e = mid - 1;
            } else {
                s = mid + 1;
            }
        }
        return 0;
    }


    public static void main(String[] args) {
        T59 t59 = new T59();
        System.out.println(t59.findM(new int[]{1, 2, 3}, 1));
        System.out.println(t59.findM(new int[]{1, 2, 3}, 0));
        System.out.println(t59.findM(new int[]{1, 2, 3}, 10));
//        System.out.println(t59.findMin(new int[]{3, 4, 5, 1, 2}));
//        System.out.println(t59.findMin(new int[]{3, 4, 5, 1, 2}));
//        System.out.println(t59.findMin(new int[]{4, 5, 6, 7, 0, 1, 2}));
//        System.out.println(t59.findMin(new int[]{11, 13, 15, 17}));
//        System.out.println(t59.findMin(new int[]{3, 1, 2}));
    }
}
