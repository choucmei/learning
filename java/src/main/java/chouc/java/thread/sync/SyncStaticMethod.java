package chouc.java.thread.sync;

/**
 * @author chouc
 * @version V1.0
 * @Title: SyncStaticMethod
 * @Package chouc.java.thread.sync
 * @Description:
 * @date 1/16/20
 */

/**
 *
 *   Synchronized
 *   1. 原理 通过获取对象的 monitor权， 获取不到时 进入同步队列   监视器锁本质又是依赖于底层的操作系统的Mutex Lock（互斥锁）来实现的。
 *
 *   1.一个类可以有多个对象，所以一个类可以有多个对象锁。
 *   2.当一个类中有多个synchronized修饰的同步方法时，其中一个方法被线程访问持有锁，其他方法同样被锁住，其他线程依然不能访问其他同步方法，因为此时锁住的是该类的对象，相当于整个对象锁住了。
 *   3.一个类只能有一个.claas，所有一个类只能有一个类锁。
 *
 *   通过 javap -c  可以看到 sync block 的 monitorenter 和 monitorexit 指令，而sync method  没有 但是 有 ACC_SYNCHRONIZED 底层都是一样
 *
 *   wait 和 notify 也是通过monitor
 *     sync method : 对象 锁
 *     sync static method : class对象 锁
 *     sync block : 锁的是 {} 中的对象。
 *   2. jdk 1.6 升级
 *    偏向锁是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁。降低获取锁的代价。其中识别是不是同一个线程一只获取锁的标志是在上面提到的对象头Mark Word（标记字段）中存储的。
 *    轻量级锁是指当锁是偏向锁的时候，被另一个线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不会阻塞，提高性能。
 *    重量级锁是指当锁为轻量级锁的时候，另一个线程虽然是自旋，但自旋不会一直持续下去，当自旋一定次数的时候，还没有获取到锁，就会进入阻塞，该锁膨胀为重量级锁。重量级锁会让其他申请的线程进入阻塞，性能降低。
 *    这时候也就成为了原始的Synchronized的实现。
 *
 *
 *
 */

public class SyncStaticMethod {

    public static synchronized void getMethod() {
        System.out.println();
    }


    public synchronized void setMethod() {
        System.out.println();
    }


    public void getObject() {
        synchronized (this) {
            System.out.println();
        }
    }

    public void setClass() {
        synchronized (SyncStaticMethod.class) {
            System.out.println();
        }
    }

    public static void main(String[] args) {

    }


}
