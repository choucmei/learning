package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C30FindSubstring
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/1/9
 */
public class C30FindSubstring {
    public List<Integer> findSubstring(String s, String[] words) {
        HashMap<String, Integer> allWord = new HashMap<>();
        for (String str : words) {
            int pre = allWord.getOrDefault(str, 0);
            allWord.put(str, pre + 1);
        }

        int wordSize = words[0].length();
        int wordCount = words.length;
        int windowSize = wordSize * wordCount;
        List<Integer> list = new ArrayList<>();
        System.out.println("windowSize:"+windowSize);
        for (int i = 0; i < s.length() - windowSize + 1; i++) {
            HashMap<String, Integer> hasWord = new HashMap<>();
            int num = 0;
            for (int j = 0; j < wordCount; j++) {
                System.out.println("i=" + i+" s:"+(i + j * wordSize)+" e:"+(i + windowSize + j * wordSize));
                String source = s.substring(i + j * wordSize, i + (j + 1) * wordSize);
                System.out.println("source:"+source);
                int targetCount = allWord.getOrDefault(source, 0);
                int sourceCount = hasWord.getOrDefault(source, 0) + 1;
                System.out.println("targetCount:" + targetCount);
                System.out.println("sourceCount:" + sourceCount);
                if (targetCount <= 0 || sourceCount > targetCount) {
                    break;
                } else {
                    hasWord.put(source,sourceCount);
                }
                num++;
            }
            System.out.println("num" + num);
            if (num == wordCount) {
                list.add(i);
            }
        }
        return list;
    }


    public static void main(String[] args) {
        String s = "wordgoodgoodgoodbestword";
        String[] words = new String[]{"word","good","best","good"};
        C30FindSubstring c = new C30FindSubstring();
        System.out.println(c.findSubstring(s, words));
    }
}
