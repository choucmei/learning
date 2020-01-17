package chouc.java.thread.model08;

/**
 * Future模式的角色如下：
 *
 * Client（委托人）参与者
 * Client参与者会向Host参与者送出请求（request），Client参与者会马上得到VirtualData，作为请求结果的返回值。（案例中的Main类就是Client）
 *
 * Host参与者
 * Host参与者接受请求（request），然后创建线程进行异步处理。Host参与者会立即返回Future（以VirturalData的形式）。
 *
 * VirtualData（虚拟数据）参与者
 * VirtualData是用来统一代表Future参与者与RealData参与者。（案例中Data接口就是VirtualData参与者）
 *
 * RealData（实际数据）参与者
 * RealData表示实际的数据。
 *
 * Future参与者
 * Future参与者包含获取实际的数据和设置实际数据的方法。Host类会创建该对象，当异步线程处理完成时，会调用Future的设置数据的方法。
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("main BEGIN");
        Host host = new Host();
        Data data1 = host.request(10, 'A');
        Data data2 = host.request(20, 'B');
        Data data3 = host.request(30, 'C');

        System.out.println("main otherJob BEGIN");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        System.out.println("main otherJob END");
        System.out.println("data1 = " + data1.getContent());
        System.out.println("data2 = " + data2.getContent());
        System.out.println("data3 = " + data3.getContent());
        System.out.println("main END");
    }
}