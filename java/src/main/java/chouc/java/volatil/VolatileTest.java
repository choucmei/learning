package chouc.java.volatil;


public class VolatileTest {

    public static volatile boolean flag = true;
    public static volatile int tem = 0;

    public static void main(String[] args) throws InterruptedException {

        // 检验volatile的线程安全
//        for (int i = 0;i < 10;i++){
//            new Thread(new Runnable() {
//                public void run() {
//                    for (int j =0 ;j<1000000;j++){
//                        tem++;
//                        System.out.println(Thread.currentThread().getName()+"。。。"+tem);
//                    }
//                }
//            },"***"+i+"***").start();
//
//        }

        //结论：非线程安全



        /******分割线********/


        for (int i = 0;i < 10;i++){
            new Thread(new Runnable() {
                public void run() {
                    long startTime = System.currentTimeMillis();
                    while (flag){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName()+"... running ");
                    }
                    long endTime = System.currentTimeMillis();
                    System.out.println(Thread.currentThread().getName()+"Time is "+(endTime-startTime));
                }
            },"***"+i+"***").start();

        }

        Thread.sleep(10000);
        flag = false;
    }
}
