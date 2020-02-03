package chouc.java.thread.juc13Phaser;

import java.io.IOException;
import java.util.concurrent.Phaser;

public class PhaserMain03 {
    public static void main(String[] args) throws IOException {

        int repeats = 3;    // 指定任务最多执行的次数

        Phaser phaser = new Phaser() {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("---------------PHASE[" + phase + "],Parties[" + registeredParties + "] ---------------");
                return phase + 1 >= repeats  || registeredParties == 0;
            }
        };

        for (int i = 0; i < 10; i++) {
            phaser.register();                      // 注册各个参与者线程
            new Thread(new PhaserTask(phaser), "Thread-" + i).start();
        }
    }
}
