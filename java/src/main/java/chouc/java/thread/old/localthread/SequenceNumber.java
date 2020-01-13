package chouc.java.thread.old.localthread;

public class SequenceNumber {
// ①通过匿名内部类覆盖ThreadLocal的initialValue()方法，指定初始值

    private static ThreadLocal seqNum = new ThreadLocal() {


        public Integer initialValue() {
            return 0;
        }

    };
// ②获取下一个序列值


    public int getNextNum() {
        seqNum.set((Integer) seqNum.get() + 1);
        return (Integer) seqNum.get();
    }


    public static void main(String[] args) {
        SequenceNumber sn = new SequenceNumber();
// ③ 3个线程共享sn，各自产生序列号
        TestClient t1 = new TestClient(sn);
        TestClient t2 = new TestClient(sn);
        TestClient t3 = new TestClient(sn);
        new Thread(t1).start();
        new Thread(t2).start();
        new Thread(t3).start();
    }

}


class TestClient implements Runnable {

    private SequenceNumber sn;


    public TestClient(SequenceNumber sn) {
        super();
        this.sn = sn;
    }


    public void run() {
        for (int i = 0; i < 3; i++) {
// ④每个线程打出3个序列值
            System.out.println("thread[" + Thread.currentThread().getName()
                    + "] sn[" + sn.getNextNum() + "]");
        }
    }

}