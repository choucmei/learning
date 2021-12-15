package chouc.hadoop.yarn.application;

public class ApplicationExecutor {
    public static void main(String[] args) {
        int total = Integer.parseInt(args[0]);
        for (int i = 0; i < total; i++) {
            System.out.println("hello world " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
