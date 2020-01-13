package chouc.java.thread.model03;

/**
 * @author chouc
 * @version V1.0
 * @Title: Main
 * @Package chouc.java.thread.model03
 * @Description:
 * @date 1/10/20
 */

/**
 *
 * Guarded Suspension
 *
 *
 *
 * GuardedObject 参与者是一个拥有被防卫的方法（guardedMethod）的类。当线程执行guardedMethod时，只要满足警戒条件，就能继续执行，否则线程会进入wait set区等待。警戒条件是否成立随着GuardedObject的状态而变化。
 * GuardedObject 参与者除了guardedMethod外，可能还有用来更改实例状态的的方法stateChangingMethod。
 * 在Java语言中，是使用while语句和wait方法来实现guardedMethod的；使用notify/notifyAll方法实现stateChangingMethod。如案例中的RequestQueue 类。
 * 注意：Guarded Suspension Pattern 需要使用while，这样可以使从wait set被唤醒的线程在继续向下执行前检查Guard条件。如果改用if，当多个线程被唤醒时，由于wait是继续向下执行的，可能会出现问题。
 *
 * getRequest方法中有一个判断while (queue.size() <= 0)，该判断称为Guarded Suspension Pattern 的警戒条件（guard condition）。
 *
 */
public class Main {
    public static void main(String[] args) {
        RequestQueue requestQueue = new RequestQueue();
        new ClientThread(requestQueue, "Alice", 3141592L).start();
        new ServerThread(requestQueue, "Bobby", 6535897L).start();
    }
}