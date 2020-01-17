package chouc.java.thread.model08;

public class Host {
    public Data request(final int count, final char c) {
        System.out.println("    request(" + count + ", " + c + ") BEGIN");
        final FutureData future = new FutureData();
        new Thread() {
            public void run() {
                RealData realdata = new RealData(count, c);
                future.setRealData(realdata);
            }
        }.start();
        System.out.println("    request(" + count + ", " + c + ") END");
        return future;
    }
}