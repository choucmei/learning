package chouc.java.thread.juc12Exchanger;

import java.util.concurrent.Exchanger;

public class ExchangerTest {
    public static void main(String[] args) {
        Exchanger<Message> exchanger = new Exchanger<>();
        Thread t1 = new Thread(new Consumer(exchanger), "消费者-t1");
        Thread t2 = new Thread(new Producer(exchanger), "生产者-t2");
        Thread t3 = new Thread(new Producer(exchanger), "生产者-t3");
        t1.start();
        t2.start();
        t3.start();
    }
}

class Message{
    String v;

    public Message(String v) {
        this.v = v;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}

class Producer implements Runnable {
    private final Exchanger<Message> exchanger;

    public Producer(Exchanger<Message> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        Message message = new Message(null);
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(1000);

                message.setV(Thread.currentThread().getName()+String.valueOf(i));
                System.out.println(Thread.currentThread().getName() + ": 生产了数据[" + Thread.currentThread().getName()+i + "]");

                message = exchanger.exchange(message);

                System.out.println(Thread.currentThread().getName() + ": 交换得到数据[" + String.valueOf(message.getV()) + "]");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

class Consumer implements Runnable {
    private final Exchanger<Message> exchanger;

    public Consumer(Exchanger<Message> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        Message msg = new Message(null);
        while (true) {
            try {
                Thread.sleep(1000);
                msg = exchanger.exchange(msg);
                System.out.println(Thread.currentThread().getName() + ": 消费了数据[" + msg.getV() + "]");
                msg.setV(null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}