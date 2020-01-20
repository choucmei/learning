package chouc.java.thread.comm01;

import java.util.Date;

/**
 * @author chouc
 * @version V1.0
 * @Title: MainInheritableThreadLocal
 * @Package chouc.java.thread.comm01
 * @Description:
 * @date 1/17/20
 */
public class MainInheritableThreadLocal {
}



class InheritableThreadLocalExt extends InheritableThreadLocal {
    @Override
    protected Object initialValue() {
        return new Date().getTime();
    }

    @Override
    protected Object childValue(Object parentValue) {
        return parentValue + " 我在子线程加的~!";
    }
}