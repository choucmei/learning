package chouc.hadoop.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class GetProvince extends UDF {

    public String evaluate(String num){
        if (num.equals("123")){
            return "anhui";
        }else if (num.equals("456")){
            return "sichuan";
        }else {
            return "no know";
        }
    }
}
