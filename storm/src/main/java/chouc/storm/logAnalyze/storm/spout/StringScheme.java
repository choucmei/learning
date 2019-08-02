package chouc.storm.logAnalyze.storm.spout;

import org.apache.storm.spout.Scheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.nio.ByteBuffer;
import java.util.List;

public class StringScheme implements Scheme {


    @Override
    public List<Object> deserialize(ByteBuffer ser) {
        byte[] bytes = new byte[ser.remaining()];
        ser.get(bytes,0, bytes.length);
        try {
            return new Values(new String(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Fields getOutputFields() {
        return new Fields("line");
    }
}