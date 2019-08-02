package chouc.hadoop.mr.wordcount;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 *
 *
 * /Users/chouc/Desktop/local_hadoop/input /Users/chouc/Desktop/local_hadoop/output  本地运行路径
 *
 *
 *
 * -DHADOOP_USER_NAME=root /word/count/file /word/count/out
 *
 *
 */
public class WordCountDriver {


//    @HBaseTest
//    public void test(){
//        System.out.println(System.getenv("HADOOP_HOME"));
//        System.out.println(System.getenv("JAVA_HOME"));
//        System.getProperties().list(System.err);
//        System.out.println("Java运行时环境版本:"+System.getProperty("java.version"));
//    }

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

//        job.setJarByClass(WordCountDriver.class);
//        job.setJar("/Users/chouc/Work/IdeaProjects/learning/out/artifacts/learning/learning.jar");


        //指定本业务job要使用的mapper/reduce 类
        job.setMapperClass(WordcountMapper.class);
        job.setReducerClass(WordcountReduce.class);
        // 设置combiner的class 类
        job.setCombinerClass(WordcountReduce.class);

        // 设置结合文件的类，控制切块个数
        job.setInputFormatClass(CombineTextInputFormat.class);
        CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);
        CombineTextInputFormat.setMinInputSplitSize(job, 2097152);

        //指定mapper输出数据的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //指定reduce输出数据的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);







        //指定job输入原文件所在目录
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        //指定job输出结果文件目的目录
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        //将job 中配置的相关参数，以及job 所用的java类所在的jar 包，提交yarn 去运行
//        job.submit();
        boolean result = job.waitForCompletion(true);
        System.exit(result?0:1);
    }

}
