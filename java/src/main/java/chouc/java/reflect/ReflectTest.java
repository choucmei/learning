package chouc.java.reflect;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectTest {
    public String className = null;
    public Class targetClass = null;


    @Before
    public void init() throws ClassNotFoundException {
        className = "chouc.java.reflect.Person";
        targetClass = Class.forName(className);

    }

    /**
     *获取某个class文件对象
     */
    @Test
    public void getClassName(){
        System.out.println(targetClass);
    }

    /**
     *创建一个class文件表示的实例对象，底层会调用空参数的构造方法
     */
    @Test
    public void getNewInstance() throws IllegalAccessException, InstantiationException {
        System.out.println(targetClass.newInstance());
    }

    /**
     *获取非私有的构造函数
     */
    @Test
    public void getPublicConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = targetClass.getConstructor(Long.class,String.class);
        Person person = (Person) constructor.newInstance(123L,"mxb");
        System.out.println(person.getId());
        System.out.println(person.getName());
    }

    /**
     *获得私有的构造函数
     */
    @Test
    public void getPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = targetClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);//强制取消Java的权限检测
        Person person = (Person) constructor.newInstance("mxb");
        System.out.println(person.getName());
    }

    /**
     *访问非私有的成员变量
     */
    @Test
    public void getNotPrivateField() throws Exception{
        Field field = targetClass.getField("name");
        Object object = targetClass.newInstance();
        System.out.println(field.get(object));
        field.set(object,"mxb");
        System.out.println(field.get(object));
    }

    /**
     *访问私有的成员变量
     */
    @Test
    public void getPrivateField() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Field field = targetClass.getDeclaredField("id");
        field.setAccessible(true);
        Object object = targetClass.newInstance();
        System.out.println(field.get(object));
        field.set(object,1L);
        System.out.println(field.get(object));
    }

    /**
     *获取非私有的成员函数
     */
    @Test
    public void getNotPrivateMethod() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object oject = targetClass.newInstance();
        Method method = targetClass.getMethod("testPublic",String.class,Long.class);
        Object object02 = method.invoke(oject,"arg 0001 ",4L);
        System.out.println(object02);
    }

    /**
     *获取私有的成员函数
     */
    @Test
    public void getPrivateMethod() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object object = targetClass.newInstance();
        Method method = targetClass.getDeclaredMethod("testPrivate");
        method.setAccessible(true);
        System.out.println(method.invoke(object));
    }

    @Test
    public void otherMethod() throws Exception {
        //当前加载这个class文件的那个类加载器对象
        System.out.println(targetClass.getClassLoader());
        //获取某个类实现的所有接口
        Class[] interfaces = targetClass.getInterfaces();
        for (Class class1 : interfaces) {
            System.out.println(class1);
        }
        //反射当前这个类的直接父类
        System.out.println(targetClass.getGenericSuperclass());
        /**
         * getResourceAsStream这个方法可以获取到一个输入流，这个输入流会关联到name所表示的那个文件上。
         */
        //path 不以’/'开头时默认是从此类所在的包下取资源，以’/'开头则是从ClassPath根下获取。其只是通过path构造一个绝对路径，最终还是由ClassLoader获取资源。
        System.out.println(targetClass.getResourceAsStream("/log4j.properties"));
        System.out.println(targetClass.getResourceAsStream("log4j.properties"));

        //判断当前的Class对象表示是否是数组
        System.out.println(targetClass.isArray());
        System.out.println(new String[3].getClass().isArray());

        //判断当前的Class对象表示是否是枚举类
        System.out.println(targetClass.isEnum());
        System.out.println(Class.forName("chouc.java.reflect.City").isEnum());

        //判断当前的Class对象表示是否是接口
        System.out.println(targetClass.isInterface());
        System.out.println(Class.forName("chouc.java.reflect.TestInterface").isInterface());


    }
}
