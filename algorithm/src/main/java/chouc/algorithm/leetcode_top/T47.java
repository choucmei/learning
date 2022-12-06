package chouc.algorithm.leetcode_top;

public class T47 {
    public String reverseWords(String str) {

        int s = 0, e = str.length() - 1;
        while (s < e && str.charAt(s) == ' ') {
            s++;
        }
        while (s < e && str.charAt(e) == ' ') {
            e--;
        }
        char[] chars = str.substring(s, e + 1).toCharArray();
        char[] reverse = reverse(chars, 0, chars.length - 1);
        for (int i = 0; i < reverse.length; i++) {
            if (reverse[i] == ' ' && reverse[i - 1] == ' ') {
                continue;
            } else {
                int j = i;
                while (j < reverse.length && reverse[j] != ' ') {
                    j++;
                }
                if (j - i != 0) {
                    reverse(reverse, i, j - 1);
                }
                i = j;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < reverse.length; i++) {
            if (reverse[i] == ' ' && reverse[i - 1] == ' ') {
                continue;
            } else {
                stringBuilder.append(reverse[i]);
            }
        }
        return stringBuilder.toString();
    }

    public char[] reverse(char[] chars, int s, int e) {
        while (s <= e) {
            swap(chars, s++, e--);
        }
        return chars;
    }

    private void swap(char[] chars, int s, int e) {
        char t = chars[s];
        chars[s] = chars[e];
        chars[e] = t;
    }

    public static void main(String[] args) {
//        输入：s = "the sky is blue"
//        输出："blue is sky the"
//
//        输入：s = "  hello world  "
//        输出："world hello"
//
//        输入：s = "a good   example"
//        输出："example good a"
        T47 t47 = new T47();
        System.out.println(t47.reverseWords("the sky is blue"));
        System.out.println(t47.reverseWords(" hello world  "));
        System.out.println(t47.reverseWords("a good   example"));

    }


}
