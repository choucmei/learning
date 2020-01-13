package chouc.java.thread.model01;

/**
 * @author chouc
 * @version V1.0
 * @Title: Main
 * @Package chouc.java.thread.review.example01
 * @Description:
 * @date 1/9/20
 */

/**
 *
 * Single Threaded Execution
 *
 *
 *  SharedResource(共享资源)参与者
 *  SharedResource就是多线线程会同时访问的资源类，该类通常具有2类方法：
 *  ①SafeMethod——从多个线程同时调用也不会发生问题的方法
 *  ②UnsafeMethod——从多个线程同时调用会发生问题，这类方法需要加以防护，指定只能由单线程访问区域，即临界区（critical section）
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("Testing Gate, hit CTRL+C to exit.");
        Gate gate = new Gate();
        new UserThread(gate, "Alice", "Alaska").start();
        new UserThread(gate, "Bobby", "Brazil").start();
        new UserThread(gate, "Chris", "Canada").start();
    }
}
