package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C57InsertRange
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/3/1
 */
public class C57InsertRange {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        int left = newInterval[0];
        int right = newInterval[1];
        boolean inserted = false;
        List<int[]> n = new ArrayList<>();
        for (int[] i : intervals) {
            if (i[0] > right) {
                if (!inserted) {
                    n.add(new int[]{left, right});
                    inserted = true;
                }
                n.add(i);
            } else if (i[1] < left) {
                n.add(i);
            } else {
                left = Math.min(left, i[0]);
                right = Math.max(right, i[1]);
            }
        }
        if (!inserted) {
            n.add(new int[]{left, right});
        }
        return n.toArray(new int[n.size()][2]);
    }


    public static void main(String[] args) {

    }
}
