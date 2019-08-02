package chouc.hadoop.mr.rjoin;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class RJoin  {

    static class RJoinMapper extends Mapper<LongWritable, Text, Text,OrderBean> {
        OrderBean orderBean = new OrderBean();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",");
            FileSplit fileSplit = (FileSplit) context.getInputSplit();
            if (fileSplit.getPath().getName().startsWith("order")){
                orderBean.set(true,fields[0],fields[1],fields[2],Integer.parseInt(fields[3]),"",0);
            }else {
                orderBean.set(false,"","",fields[0],0,fields[1],Float.parseFloat(fields[2]));
            }
            context.write(new Text(orderBean.getGoodsId()),orderBean);
        }
    }

    static class RJoinReduce extends Reducer<Text,OrderBean,OrderBean, NullWritable>{
        OrderBean goodsBean = new OrderBean();
        List<OrderBean> orderBeans = new ArrayList<OrderBean>();
        @Override
        protected void reduce(Text key, Iterable<OrderBean> values, Context context) throws IOException, InterruptedException {
            for (OrderBean orderBeanValue:values){
                if (orderBeanValue.isFlag()){
                    OrderBean temporaryBean = new OrderBean();
                    try {
                        BeanUtils.copyProperties(temporaryBean,orderBeanValue);
                        orderBeans.add(temporaryBean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        BeanUtils.copyProperties(goodsBean,orderBeanValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

            for (OrderBean orderBean:orderBeans){
                orderBean.setGoodsName(goodsBean.getGoodsName());
                orderBean.setPrice(goodsBean.getPrice());
                context.write(orderBean, NullWritable.get());
            }
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 设置本地的hadoop 路径
        System.setProperty("hadoop.home.dir","/Users/chouc/Desktop/local_hadoop/hadoop-2.9.0");

        Configuration configuration = new Configuration();


        //是否运行为本地模式，就是看这个参数值是否为local，默认就是local
		/*conf.set("mapreduce.framework.name", "local");*/

        //本地模式运行mr程序时，输入输出的数据可以在本地，也可以在hdfs上
        //到底在哪里，就看以下两行配置你用哪行，默认就是file:///
//      configuration.set("fs.defaultFS", "hdfs://s1/");  hdfs
//		configuration.set("fs.defaultFS", "file:///"); 本地

        //运行集群模式，就是把程序提交到yarn中去运行
        //要想运行为集群模式，以下3个参数要指定为集群上的值
        configuration.set("mapreduce.framework.name", "yarn");
        configuration.set("yarn.resourcemanager.hostname", "s1");
        configuration.set("fs.defaultFS", "hdfs://s1/");

        Job job = Job.getInstance(configuration);

        //指定本业务job要使用的mapper/reduce 类
        job.setMapperClass(RJoinMapper.class);
        job.setReducerClass(RJoinReduce.class);

        // 设置结合文件的类，控制切块个数
        job.setInputFormatClass(CombineTextInputFormat.class);
        CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);
        CombineTextInputFormat.setMinInputSplitSize(job, 2097152);

        //指定mapper输出数据的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(OrderBean.class);
        //指定reduce输出数据的kv类型
        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);







        //指定job输入原文件所在目录
        FileInputFormat.setInputPaths(job,new org.apache.hadoop.fs.Path(args[0]));
        //指定job输出结果文件目的目录
        FileOutputFormat.setOutputPath(job,new org.apache.hadoop.fs.Path(args[1]));

        //将job 中配置的相关参数，以及job 所用的java类所在的jar 包，提交yarn 去运行
//        job.submit();
        boolean result = job.waitForCompletion(true);
        System.exit(result?0:1);
    }


}
