package chouc.java.thread.old.synchronization;

/**
 * @author chouc
 * @version V1.0
 * @Title: SyschronizeAll
 * @Package chouc.java.thread.old.synchronization
 * @Description:
 * @date 9/11/19
 */
public class SyschronizeAll {

    public void methodCodeBlock(){
        synchronized (this){
            System.out.println("method Code Block");
        }
    }


    synchronized public void methodSys(){
        System.out.println("method synchronized");
    }

    public static void main(String[] args) {
        System.out.println(1);
    }
}
