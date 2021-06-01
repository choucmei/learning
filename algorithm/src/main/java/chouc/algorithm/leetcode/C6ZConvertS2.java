package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C6ZConvert
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/11/30
 */
public class C6ZConvertS2 {
    public static String convert(String s, int numRows) {
        if (numRows==1){
            return s;
        }
        List<StringBuilder> listSb = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            listSb.add(new StringBuilder());
        }
        int currentRow = 0;
        boolean isReverse = false;
        for (Character c : s.toCharArray()) {
            listSb.get(currentRow).append(c);
            if (currentRow == 0 || currentRow == numRows - 1) {
                isReverse = !isReverse;
            }
            currentRow += isReverse ? 1 : -1;
        }
        StringBuilder finallySb = new StringBuilder();
        for (StringBuilder sb : listSb) {
            finallySb.append(sb);
        }
        return finallySb.toString();
    }


    public static void main(String[] args) {
        String a = "LEETCODEISHIRING";
        System.out.println(convert(a, 4));
    }
}
