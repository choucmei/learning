package chouc.java.reflect;

import java.util.HashMap;
import java.util.Map;

public class StaticClass {

    static Map<String,String> map = new HashMap<>();

    static {
        System.out.println(" ---- ");
        map.put("test","test");
    }
}
