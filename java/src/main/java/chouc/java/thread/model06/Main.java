package chouc.java.thread.model06;

/**
 * Thread-Per-Message模式的角色如下：
 *
 * Client(委托人)参与者
 * Client参与者会对Host参与者送出请求(Request)。上述案例中，Client参与者就是Main类。
 *
 * Host参与者
 * Host参与者接受来自Client的请求，然后建立新的线程处理它。
 *
 * Helper(帮助者)参与者
 * Helper实际处理请求的。
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("main BEGIN");
        Host host = new Host();
        host.request(10, 'A');
        host.request(20, 'B');
        host.request(30, 'C');
        System.out.println("main END");
    }
}