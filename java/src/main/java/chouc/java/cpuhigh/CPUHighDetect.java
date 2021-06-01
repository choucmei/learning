package chouc.java.cpuhigh;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: CPUHighDetect
 * @Package chouc.java.cpuhigh
 * @Description:
 * @date 2021/3/8
 */
public class CPUHighDetect {
    public static void main(String[] args) {
        new Thread(new Job1(1)).start();
        new Thread(new Job2(2)).start();
        new Thread(new Job3(3)).start();
    }
}

abstract class Job implements Runnable {
    protected int jobId;
    protected Date date;
    protected Random r = new Random();
}

class Job1 extends Job {
    public Job1(int jonId) {
        this.jobId = jonId;
        this.date = new Date();
    }

    @Override
    public void run() {
        int v = r.nextInt(100);
        boolean state = v % 2 == 0;
        if (state) {
            System.out.println("CREATE LOOP "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
        } else {
            System.out.println("CREATE BLOCK "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
        }
        while (state){
            System.out.println("LOOP "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
        }
        try {
            System.out.println("BLOCK "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Job2 extends Job {
    public Job2(int jonId) {
        this.jobId = jonId;
        this.date = new Date();
    }

    @Override
    public void run() {
        int v = r.nextInt(100);
        boolean state = v % 2 == 0;
        if (state) {
            System.out.println("CREATE LOOP "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
        } else {
            System.out.println("CREATE BLOCK "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
        }
        while (state){
            System.out.println("LOOP "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
        }
        try {
            System.out.println("BLOCK "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Job3 extends Job {
    public Job3(int jonId) {
        this.jobId = jonId;
        this.date = new Date();
    }

    @Override
    public void run() {
        int v = r.nextInt(100);
        boolean state = v % 2 == 0;
        if (state) {
            System.out.println("CREATE LOOP "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
        } else {
            System.out.println("CREATE BLOCK "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
        }
        while (state){
            if (state){
                System.out.println("LOOP "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
            } else {
                try {
                    System.out.println("BLOCK "+" class:"+getClass().getName()+" jobid"+jobId+" date:"+date);
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}