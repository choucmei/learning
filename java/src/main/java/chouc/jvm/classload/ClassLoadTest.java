package chouc.jvm.classload;

/**
 * @author chouc
 * @version V1.0
 * @Title: ClassLoadTest
 * @Package chouc.classload
 * @Description:
 * @date 7/18/19
 */
public class ClassLoadTest {
    public static void main(String[] args) {
        ClassLoader classLoader = ClassLoadTest.class.getClassLoader();
        ClassLoader parentClassload = classLoader.getParent();
        ClassLoader ppClassload = parentClassload.getParent();
        System.out.println();
    }
}
