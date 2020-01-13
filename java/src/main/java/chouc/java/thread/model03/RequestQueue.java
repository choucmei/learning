package chouc.java.thread.model03;

import java.util.LinkedList;

/**
 * @author chouc
 * @version V1.0
 * @Title: RequestQueue
 * @Package chouc.java.thread.model03
 * @Description:
 * @date 1/10/20
 */
public class RequestQueue {
    private final LinkedList<Request> queue = new LinkedList<Request>();
    public synchronized Request getRequest() {
        while (queue.size() <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        return (Request)queue.removeFirst();
    }
    public synchronized void putRequest(Request request) {
        queue.addLast(request);
        notifyAll();
    }
}
