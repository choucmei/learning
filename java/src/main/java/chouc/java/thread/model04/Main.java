package chouc.java.thread.model04;

/**
 * @author chouc
 * @version V1.0
 * @Title: Main
 * @Package chouc.java.thread.model04
 * @Description:
 * @date 1/10/20
 */

/**
 *  Balking 模式
 *
 *  GuardedObject(被警戒的对象)参与者
 *
 *  GuardedObject参与者是一个拥有被警戒的方法(guardedMethod)的类。当线程执行guardedMethod时，只有满足警戒条件时，才会继续执行，否则会立即返回。警戒条件的成立与否，会随着GuardedObject参与者的状态而变化。
 *
 *  注：上述示例中，Data类就是GuardedObject(被警戒的对象)参与者，save方法是guardedMethod，change方法是stateChangingMethod。
 */
public class Main {
    public static void main(String[] args) {
        Data data = new Data("data.txt", "(empty)");
        new ChangerThread("ChangerThread", data).start();
        new SaverThread("SaverThread", data).start();
    }
}