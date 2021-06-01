package chouc.algorithm.leetcode;

import java.util.HashMap;

/**
 * @author chouc
 * @version V1.0
 * @Title: C9A2OI
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/3
 */
public class C8A2OI {
    public static int myAtoi(String s) {
        int resv = 0;
        s = s.trim();
        if (s.length() == 0) {
            return 0;
        }
        int yizi = 1;
        char first = s.charAt(0);
        if (first == 45 || first == 43) {
            if (first == 45) {
                yizi = -1;
            }
            s = s.substring(1, s.length());
        }
        int pop = 0;
        for (Character ch : s.toCharArray()) {
            if (ch <= 57 && ch >= 48) {
                pop = (ch - 48) * yizi;
                if (resv > Integer.MAX_VALUE / 10 || (resv == Integer.MAX_VALUE / 10 && pop > 7))
                    return Integer.MAX_VALUE;
                if (resv < Integer.MIN_VALUE / 10 || (resv == Integer.MIN_VALUE / 10 && pop < -8))
                    return Integer.MIN_VALUE;
                resv = resv * 10 + pop;
            } else {
                return resv;
            }
        }
        return resv;
    }


    public int atoi(String s) {
        Computer automaton = new Computer();
        int length = s.length();
        for (int i = 0; i < length; ++i) {
            automaton.put(s.charAt(i));
        }
        return (int) (automaton.sign * automaton.ans);

    }

    class Computer {
        public int sign = 1;
        public long ans = 0;
        private String state = "start";
        private HashMap<String, String[]> table = new HashMap<>();

        {
            table.put("start", new String[]{"start", "sign", "number", "end"});
            table.put("sign", new String[]{"end", "end", "number", "end"});
            table.put("number", new String[]{"end", "end", "number", "end"});
            table.put("end", new String[]{"end", "end", "end", "end"});
        }


        private int getType(char c) {
            if (c == ' ') {
                return 0;
            }
            if (c == '+' || c == '-') {
                return 1;
            }
            if (Character.isDigit(c)) {
                return 2;
            }
            return 3;
        }

        public void put(char c){
            state = table.get(state)[getType(c)];
            if (state.equals("number")){
                ans = ans*10 + c - '0';
                ans = sign == 1 ? Math.min(ans, (long) Integer.MAX_VALUE) : Math.min(ans, -(long) Integer.MIN_VALUE);
            }else if ("signed".equals(state)) {
                sign = c == '+' ? 1 : -1;
            }
        }

    }

    public static void main(String[] args) {
        System.out.println(myAtoi("-42"));
    }
}
