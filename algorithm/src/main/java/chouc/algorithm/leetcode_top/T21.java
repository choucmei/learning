package chouc.algorithm.leetcode_top;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


//nums = [1,2,3]
//[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]

public class T21 {
    public List<List<Integer>> permute(int[] nums) {
        ArrayList<List<Integer>> objects = new ArrayList<>();
        List<Integer> collect = Arrays.stream(nums).boxed().collect(Collectors.toList());
        traceback(objects, collect, 0);
        return objects;
    }

    public void traceback(List<List<Integer>> list, List<Integer> nums, int i) {
        if (i >= nums.size()) {
            list.add(new ArrayList<>(nums));
            return;
        }
        for (int k = i; k < nums.size(); k++) {
            Collections.swap(nums,i,k);
            traceback(list, nums, i + 1);
            Collections.swap(nums,i,k);
        }
    }

    public static void main(String[] args) {
        T21 t21 = new T21();
        System.out.println(t21.permute(new int[]{1, 2, 3}));
    }
}
