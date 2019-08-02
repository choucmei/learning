package chouc.hadoop.mr.secondarysort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class SecondarySort {

    static OrderBean orderBean = new OrderBean();

    static class SecondarySortMapper extends Mapper<LongWritable, Text,OrderBean, NullWritable>{
        @Override
        protected void map(LongWritable key, Text values, Context context) throws IOException, InterruptedException {
            String[] field = values.toString().split(",");
            orderBean.set(new Text(field[0]),new DoubleWritable(Float.parseFloat(field[2])));
            context.write(orderBean, NullWritable.get());
        }
    }

    static class SecondarySortReduce extends Reducer<OrderBean, NullWritable,OrderBean, NullWritable>{
        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }


    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(SecondarySort.class);

        job.setMapperClass(SecondarySortMapper.class);
        job.setReducerClass(SecondarySortReduce.class);


        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, new Path("/Users/chouc/Desktop/local_hadoop/case/secondarysort/input"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/chouc/Desktop/local_hadoop/case/secondarysort/out1"));

        //在此设置自定义的Groupingcomparator类
        job.setGroupingComparatorClass(ItemidGroupingComparator.class);
        //在此设置自定义的partitioner类
//        job.setPartitionerClass(ItemIdPartitioner.class);
//
//        job.setNumReduceTasks(2);

        job.waitForCompletion(true);

    }

}
