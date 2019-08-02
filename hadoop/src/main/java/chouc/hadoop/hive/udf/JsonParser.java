package chouc.hadoop.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonParser extends UDF {


    public String evaluate(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MovieRateBean movieRateBean = objectMapper.readValue(json,MovieRateBean.class);
            return movieRateBean.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return " error ";
        }
    }

}
