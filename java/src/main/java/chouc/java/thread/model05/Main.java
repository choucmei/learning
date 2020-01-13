package chouc.java.thread.model05;

/**
 * @author chouc
 * @version V1.0
 * @Title: Main
 * @Package chouc.java.thread.model05
 * @Description:
 * @date 1/10/20
 */

/**
 * Producer-Consumer模式的角色如下：
 *
 *     Data(数据)参与者
 *
 * Data代表了实际生产或消费的数据。
 *
 *     Producer(生产者)参与者
 *
 * Producer会创建Data，然后传递给Channel参与者。
 *
 *     Consumer(消费者)参与者
 *
 * Consumer从Channel参与者获取Data数据，进行处理。
 *
 *     Channel(通道)参与者
 *
 * Channel从Producer参与者处接受Data参与者，并保管起来，并应Consumer参与者的要求，将Data参与者传送出去。为确保安全性，Producer参与者与Consumer参与者要对访问共享互斥。
 *
 */
public class Main {
    public static void main(String[] args) {
        Table table = new Table(3);
        new MakerThread("MakerThread-1", table, 31415).start();
        new MakerThread("MakerThread-2", table, 92653).start();
        new MakerThread("MakerThread-3", table, 58979).start();
        new EaterThread("EaterThread-1", table, 32384).start();
        new EaterThread("EaterThread-2", table, 62643).start();
        new EaterThread("EaterThread-3", table, 38327).start();
    }
}