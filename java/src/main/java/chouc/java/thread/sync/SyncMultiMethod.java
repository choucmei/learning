package chouc.java.thread.sync;

import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: SyncMultiMethod
 * @Package chouc.java.thread.sync
 * @Description:
 * @date 1/16/20
 */
public class SyncMultiMethod {

    private String value;

    public synchronized String getValue() {
        System.out.println(Thread.currentThread().getName() + " getValue enter" + value);
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " getValue exit" + value);
        return value;
    }

    public synchronized void setValue(String value) {
        System.out.println(Thread.currentThread().getName() + " setValue enter" + value);
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " setValue exit" + value);
        this.value = value;
    }

    public void printlnValue() {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + " printlnValue enter" + value);
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " printlnValue exit" + value);
        }
    }

    public static void main(String[] args) {
        SyncMultiMethod syncMultiMethod = new SyncMultiMethod();
        Thread t1 = new SetClass(syncMultiMethod, " 111111");
        Thread t2 = new GetClass(syncMultiMethod);
        Thread t3 = new PrinlntClass(syncMultiMethod);
        t1.setName(" t1 ");
        t2.setName(" t2 ");
        t3.setName(" t3 ");

        t1.start();
        t2.start();
        t3.start();
    }
}





class SetClass extends Thread {

    private SyncMultiMethod multiMethod;
    private String value;

    public SetClass(SyncMultiMethod multiMethod, String value) {
        this.multiMethod = multiMethod;
        this.value = value;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start");
        multiMethod.setValue(value);
        multiMethod.getValue();
    }
}

class GetClass extends Thread {

    private SyncMultiMethod multiMethod;

    public GetClass(SyncMultiMethod multiMethod) {
        this.multiMethod = multiMethod;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start");
        multiMethod.getValue();
    }
}


class PrinlntClass extends Thread {

    private SyncMultiMethod multiMethod;

    public PrinlntClass(SyncMultiMethod multiMethod) {
        this.multiMethod = multiMethod;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start");
        multiMethod.printlnValue();
    }
}