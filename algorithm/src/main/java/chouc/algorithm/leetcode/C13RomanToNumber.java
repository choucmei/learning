package chouc.algorithm.leetcode;

import java.util.HashMap;

/**
 * @author chouc
 * @version V1.0
 * @Title: C13RomanToNumber
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/7
 */
public class C13RomanToNumber {

    String strings[] = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    HashMap<String, Integer> kv = new HashMap<>();

    {
        kv.put("CM", 900);
        kv.put("CD", 400);
        kv.put("XC", 90);
        kv.put("XL", 40);
        kv.put("IX", 9);
        kv.put("IV", 4);
        kv.put("MMM", 3000);
        kv.put("CCC", 300);
        kv.put("XXX", 30);
        kv.put("III", 3);
        kv.put("MM", 2000);
        kv.put("CC", 200);
        kv.put("XX", 20);
        kv.put("II", 2);
        kv.put("M", 1000);
        kv.put("C", 100);
        kv.put("X", 10);
        kv.put("I", 1);
        kv.put("D", 500);
        kv.put("L", 50);
        kv.put("V", 5);
    }

    public int romanToInt(String s) {
        int result = 0;
        int l = s.length();
        for (int i = 0; i < l; ) {
            if (i + 3 <= l && kv.get(s.substring(i, i + 3)) != null) {
                result += kv.get(s.substring(i, i + 3));
                i = i + 3;
            } else if (i + 2 <= l && kv.get(s.substring(i, i + 2)) != null) {
                result += kv.get(s.substring(i, i + 2));
                i = i + 2;
            } else {
                result += kv.get(s.substring(i, i + 1));
                i = i + 1;
            }
        }
        return result;
    }

    public int romanToInt2(String s) {
        int result = 0, pre = 0, cur = 0;
        for (int i = 0; i < s.length(); i++) {
            cur = getValue(s.charAt(i));
            if (pre >= cur) {
                result += cur;
            } else {
                result = result + cur - pre * 2 ;
            }
            pre = cur;
        }
        return result;
    }

    public int getValue(char c) {
        switch (c) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }

    public static void main(String[] args) {
        C13RomanToNumber c = new C13RomanToNumber();
        System.out.println(c.romanToInt2("III"));
    }


}
