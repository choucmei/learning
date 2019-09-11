package chouc.jdk8;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: AdditionLambda
 * @Package chouc.jdk8
 * @Description:
 * @date 9/10/19
 */
public class AdditionLambda {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        Collections.sort(names, (String a, String b) -> b.compareTo(a));
    }
}
