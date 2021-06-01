package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C56MergeRange
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/3/1
 */
public class C56MergeRange {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
        List<int[]> n = new ArrayList<>();
        n.add(intervals[0]);
        Utils.printArrayInArray(intervals);

        for (int i=1;i<intervals.length;i++){
            int l = intervals[i][0];
            int r = intervals[i][1];
            int maxR = n.get(n.size()-1)[1];
            if (l > maxR){
                n.add(intervals[i]);
            }else {
                n.get(n.size()-1)[1] = Math.max(maxR,r);
            }
        }
        return n.toArray(new int[n.size()][]);
    }

    public static void main(String[] args) {
        C56MergeRange c = new C56MergeRange();
        int[][] a = new int[][]{{1,3},{2,6},{8,10},{15,18}};
        Utils.printArrayInArray(c.merge(a));
    }
}
