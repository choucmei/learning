package chouc.java.thread.model02;

/**
 * @author chouc
 * @version V1.0
 * @Title: Main
 * @Package chouc.java.thread.model02
 * @Description:
 * @date 1/10/20
 */

/**
 *
 * Immutable模式
 *
 * Immutable参与者是一个字段值无法更改的类，也没有任何用来更改字段值的方法。当Immutable参与者的实例建立后，状态就完全不再变化。
 * Immutable模式的优点在于，“不需要使用synchronized保护”。
 * 而“不需要使用synchronized保护”的最大优点就是可在不丧失安全性与生命性的前提下，提高程序的执行性能。
 * 若实例由多数线程所共享，且访问非常频繁，Immutable模式就能发挥极大的优点。
 */


public class Main {
    public static void main(String[] args) {
        Person alice = new Person("Alice", "Alaska");
        new PrintPersonThread(alice).start();
        new PrintPersonThread(alice).start();
        new PrintPersonThread(alice).start();
    }
}