package chouc.algorithm.leetcode;

import chouc.algorithm.common.Utils;

import java.util.Arrays;

/**
 * @author chouc
 * @version V1.0
 * @Title: C14SamePrefix
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/8
 */
public class C14LongestCommonPrefix {
    public String longestCommonPrefix(String[] strs) {
        int minLength = Integer.MAX_VALUE;
        for (String str : strs) {
            minLength = Math.min(minLength, str.length());
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < minLength; i++) {
            boolean res = true;
            for (int j=0;j<strs.length-1;j++){
                if (strs[j].charAt(i) == strs[j+1].charAt(i)){
                    continue;
                } else {
                    res = false;
                    break;
                }
            }
            if (!res){
                break;
            } else {
                sb.append(strs[0].charAt(i));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        C14LongestCommonPrefix c = new C14LongestCommonPrefix();
        String[] a = new String[]{"flower","flow","flight"};
        Arrays.sort(a);
        Utils.printStringArray(a);
//        System.out.println(a);
//        System.out.println(c.longestCommonPrefix(a));;
    }
}
