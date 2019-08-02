package chouc.hadoop.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class ToLowerCase extends UDF {


    //必须是public
    public String evaluate(String field){
        String result = field.toLowerCase();
        return result;
    }

}
