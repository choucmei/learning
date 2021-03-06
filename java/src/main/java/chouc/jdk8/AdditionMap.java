package chouc.jdk8;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chouc
 * @version V1.0
 * @Title: AdditionMap
 * @Package chouc.jdk8
 * @Description:
 * @date 9/10/19
 */
public class AdditionMap {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }

        map.forEach((id, val) -> System.out.println(val));
        String value = null;
        value = map.computeIfPresent(3, (num, val) -> val + num + 1);
        System.out.println(value);
        value = map.get(3);             // val33
        System.out.println(value);
        map.computeIfPresent(9, (num, val) -> null);
        map.containsKey(9);     // false

        value = map.computeIfAbsent(23, num -> "val" + num);
        System.out.println(value);
        System.out.println(map.get(23));

        map.computeIfAbsent(3, num -> "bam");
        map.get(3);             // val33



        map.getOrDefault(42, "not found");  // not found
    }
}
