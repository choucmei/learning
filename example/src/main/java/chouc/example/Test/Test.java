package chouc.example.Test;

import java.util.*;

/**
 * @author chouc
 * @version V1.0
 * @Title: Test
 * @Package chouc.example.Test
 * @Description:
 * @date 9/4/19
 */
public class Test {
    public static void main(String[] args) {
//        Object key = new Object();
//        System.out.println(key.hashCode());
//        int h;
//        System.out.println((h = key.hashCode()) ^ (h >>> 16));

        int n = 15;
        int i = (n - 1) & 2;
        System.out.println(i);


        Map<String,String> map = new HashMap<String,String>();
        map.put("1222","1");
        map.put("2111","1");
        map.put("2","1");
        map.put("1","1");
        map.put("1","1");

        Test test = null;
//        System.out.println(.equals("123"));
        System.out.println(Objects.equals(null,"SnailClimb"));

    }
}
