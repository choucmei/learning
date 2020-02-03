package chouc.java.thread.juc13Phaser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Phaser;

public class PhaserMain02 {
    public static void main(String[] args) throws IOException {
        Phaser phaser = new Phaser(1);       // 注册主线程,当外部条件满足时,由主线程打开开关
        for (int i = 0; i < 10; i++) {
            phaser.register();                      // 注册各个参与者线程
            new Thread(new PhaserTask(phaser), "Thread-" + i).start();
        }

        // 外部条件:等待用户输入命令
        System.out.println("Press ENTER to continue");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();

        // 打开开关
        phaser.arriveAndDeregister();
        System.out.println("主线程打开了开关");
    }
}
