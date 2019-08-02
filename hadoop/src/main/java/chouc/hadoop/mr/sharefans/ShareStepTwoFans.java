package chouc.hadoop.mr.sharefans;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Arrays;


public class ShareStepTwoFans {


    static class ShareStepTwoFansMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text values, Context context) throws IOException, InterruptedException {
            String ling = values.toString();
            String[] tem = ling.split(":");
            String[] persons = tem[1].split(",");
            Arrays.sort(persons);
            for (int i = 0;i<persons.length-1;i++) {
                for (int j = i+1;j<persons.length;j++){
                    context.write(new Text(persons[i]+"-"+persons[j]),new Text(tem[0]));
                }
            }
        }
    }


    static class ShareStepTwoFansReduce extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuffer stringBuffer = new StringBuffer();
            for (Text text:values){
                stringBuffer.append(text.toString()).append(",");
            }
            context.write(new Text(key),new Text(stringBuffer.delete(stringBuffer.length()-1,stringBuffer.length()).toString()));
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        System.setProperty("hadoop.home.dir","/Users/chouc/Desktop/local_hadoop/hadoop-2.9.0");
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        job.setMapperClass(ShareStepTwoFansMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(ShareStepTwoFansReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 指定需要缓存一个文件到所有的maptask运行节点工作目录
		/* job.addArchiveToClassPath(archive); */// 缓存jar包到task运行节点的classpath中
		/* job.addFileToClassPath(file); */// 缓存普通文件到task运行节点的classpath中
		/* job.addCacheArchive(uri); */// 缓存压缩包文件到task运行节点的工作目录
		/* job.addCacheFile(uri) */// 缓存普通文件到task运行节点的工作目录


        FileInputFormat.setInputPaths(job,new Path("/Users/chouc/Desktop/local_hadoop/case/sharefans/out1"));
        FileOutputFormat.setOutputPath(job,new Path("/Users/chouc/Desktop/local_hadoop/case/sharefans/out2"));
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }

}
