package chouc.hadoop.mr.flowcountsort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


public class FlowCountSort {

    static class FlowCountSortMapper extends Mapper <LongWritable, Text, FlowBean, Text>{
        FlowBean flowBean = new FlowBean();
        @Override
        protected void map(LongWritable key, Text values, Context context) throws IOException, InterruptedException {
            String line = values.toString();
            String[] fields = line.split("\t");
            //取出手机号
            String phoneNbr = fields[1];
            //取出上行流量下行流量
            long upFlow = Long.parseLong(fields[fields.length-3]);
            long dFlow = Long.parseLong(fields[fields.length-2]);
            flowBean.setBean(upFlow,dFlow);
            context.write(flowBean,new Text(phoneNbr));
        }
    }


    static class FlowCountSortReduer extends Reducer <FlowBean, Text, Text,FlowBean>{
        Text pNumer = null;
        @Override
        protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value:values){
                pNumer = value;
            }
            context.write(pNumer,key);
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();

		/*conf.set("mapreduce.framework.name", "yarn");
		conf.set("yarn.resoucemanager.hostname", "mini1");*/
        Job job = Job.getInstance(conf);

		/*job.setJar("/home/hadoop/wc.jar");*/
        //指定本程序的jar包所在的本地路径
        job.setJarByClass(FlowCountSort.class);

        //指定本业务job要使用的mapper/Reducer业务类
        job.setMapperClass(FlowCountSortMapper.class);
        job.setReducerClass(FlowCountSortReduer.class);

        //指定mapper输出数据的kv类型
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        //指定最终输出的数据的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //指定job的输入原始文件所在目录
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        //指定job的输出结果所在目录
        Path path = new Path(args[1]);
        FileOutputFormat.setOutputPath(job, path);

        FileSystem fileSystem = FileSystem.newInstance(conf);
        if (fileSystem.exists(path)){
            fileSystem.delete(path);
        }

        //将job中配置的相关参数，以及job所用的java类所在的jar包，提交给yarn去运行
		/*job.submit();*/
        boolean res = job.waitForCompletion(true);
        System.exit(res?0:1);
    }



}
