package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C53MaxSubArray
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/2/25
 */
public class C53MaxSubArray {
    public class Status {
        public int lSum, rSum, mSum, iSum;

        public Status(int lSum, int rSum, int mSum, int iSum) {
            this.lSum = lSum;
            this.rSum = rSum;
            this.mSum = mSum;
            this.iSum = iSum;
        }
    }

    public int maxSubArray(int[] nums) {
        return getInfo(nums, 0, nums.length - 1).mSum;
    }

    public Status getInfo(int[] a, int l, int r) {
        if (l == r) {
            return new Status(a[l], a[l], a[l], a[l]);
        }
        int m = (l + r) >> 1;
        Status lSub = getInfo(a, l, m);
        Status rSub = getInfo(a, m + 1, r);
        return pushUp(lSub, rSub);
    }

    public Status pushUp(Status l, Status r) {
        int iSum = l.iSum + r.iSum;
        int lSum = Math.max(l.lSum, l.iSum + r.lSum);
        int rSum = Math.max(r.rSum, r.iSum + l.rSum);
        int mSum = Math.max(Math.max(l.mSum, r.mSum), l.rSum + r.lSum);
        return new Status(lSum, rSum, mSum, iSum);
    }


    public static void main(String[] args) {
        C53MaxSubArray c = new C53MaxSubArray();
        System.out.println((1 + 5) >> 1);
        System.out.println(c.maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
    }
}
