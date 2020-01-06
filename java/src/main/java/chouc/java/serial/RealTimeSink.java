package chouc.java.serial;

import java.io.Serializable;

/**
 * @author chouc
 * @version V1.0
 * @Title: RealTimeSink
 * @Package chouc.java.serial
 * @Description:
 * @date 11/11/19
 */
public class RealTimeSink implements Serializable {
//    NRealTimeSink nRealTimeSink = new NRealTimeSink();

    public void process() {
        System.out.println("*************************");
        NRealTimeSink.process();
    }

}
