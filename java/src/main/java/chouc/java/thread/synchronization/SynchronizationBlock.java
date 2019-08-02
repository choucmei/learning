package chouc.java.thread.synchronization;

import java.util.Random;

public class SynchronizationBlock {

    public void handleBusiness01(final SynchronizationBlock synchronizationBlock){
        new Thread() {
            @Override
            public void run() {
//                synchronized (synchronizationBlock){
                    System.out.println("handleBusiness01 sleep");
                    Random random = new Random();
                    try {
                        Thread.sleep(random.nextInt(10)*100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("handleBusiness01 weak");
//                }
            }
        }.start();
    }

    public void handleBusiness02(final SynchronizationBlock synchronizationBlock){
        new Thread() {
            @Override
            public void run() {
//                synchronized (synchronizationBlock){
                    System.out.println("handleBusiness02 sleep");
                    Random random = new Random();
                    try {
                        Thread.sleep(random.nextInt(10)*100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("handleBusiness02 weak");
//                }
            }
        }.start();
    }

    public static void main(String[] args) {
        final SynchronizationBlock synchronizationBlock = new SynchronizationBlock();
//        synchronizationBlock.handleBusiness01(synchronizationBlock);
//        synchronizationBlock.handleBusiness02(synchronizationBlock);


        //mycase 2
        new Thread(){
            @Override
            public void run() {
                synchronized (synchronizationBlock){
                    System.out.println("handleBusiness01 sleep");
                    Random random = new Random();
                    try {
                        Thread.sleep(random.nextInt(10)*100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("handleBusiness01 weak");
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                synchronized (synchronizationBlock){
                    System.out.println("handleBusiness02 sleep");
                    Random random = new Random();
                    try {
                        Thread.sleep(random.nextInt(10)*100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("handleBusiness02 weak");
                }
            }
        }.start();
    }

}
