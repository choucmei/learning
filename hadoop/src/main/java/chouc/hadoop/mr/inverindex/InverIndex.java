package chouc.hadoop.mr.inverindex;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class InverIndex {

    static class IntegerMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            
        }
    }


    ///Users/chouc/Desktop/local_hadoop/case/inverindex/input

}
