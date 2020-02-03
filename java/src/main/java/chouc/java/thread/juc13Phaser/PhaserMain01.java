package chouc.java.thread.juc13Phaser;

import java.util.concurrent.Phaser;

public class PhaserMain01 {
    public static void main(String[] args) {
        Phaser phaser = new Phaser();
        for (int i = 0; i < 10; i++) {
            phaser.register();                  // 注册各个参与者线程
            new Thread(new PhaserTask(phaser), "Thread-" + i).start();
        }
    }
}


class PhaserTask implements Runnable {
    private final Phaser phaser;

    PhaserTask(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "started");
        int i = phaser.arriveAndAwaitAdvance();     // 等待其它参与者线程到达
        // do something
        System.out.println(Thread.currentThread().getName() + "working");
        System.out.println(Thread.currentThread().getName() + ": 执行完任务，当前phase =" + i + "");
    }
}