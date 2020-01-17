package chouc.java.thread.model07;

/**
 * Work Thread模式的角色如下：
 *
 * Client（委托人）参与者
 * Client参与者会创建请求(Request)，然后传送给Channel参与者。
 *
 * Channel（通道）参与者
 * Channel参与者保存Request请求队列，同时会预创建Worker线程。
 *
 * Worker（工人）参与者
 * Worker参与者会从Channel获取Request。
 *
 * Request（请求）参与者
 * Worker参与者会从Channel获取Request。
 */
public class Main {
    public static void main(String[] args) {
        Channel channel = new Channel(5);
        channel.startWorkers();
        new ClientThread("Alice", channel).start();
        new ClientThread("Bobby", channel).start();
        new ClientThread("Chris", channel).start();
    }
}