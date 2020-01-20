package chouc.java.thread.wait01;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: MyList
 * @Package chouc.java.thread.vol02
 * @Description:
 * @date 1/17/20
 */
public class MyList {
    private static List<String> list = new ArrayList<String>();

    public static void add() {
        list.add("anyString");
    }

    public static int size() {
        return list.size();
    }

}
