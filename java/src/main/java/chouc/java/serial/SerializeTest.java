package chouc.java.serial;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author chouc
 * @version V1.0
 * @Title: SerializeTest
 * @Package chouc.java
 * @Description:
 * @date 11/11/19
 */
public class SerializeTest {
    public static void main(String[] args) throws IOException {
        RealTimeSink realTimeSink = new RealTimeSink();
        FileOutputStream fileOut = new FileOutputStream("/Users/chouc/Desktop/file/obj.info");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(realTimeSink);
        out.close();
        fileOut.close();
    }
}
