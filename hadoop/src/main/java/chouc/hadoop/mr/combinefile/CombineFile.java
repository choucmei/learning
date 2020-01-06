package chouc.hadoop.mr.combinefile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class CombineFile extends Configured implements Tool {


    static class CombineFileMapper extends Mapper<NullWritable, BytesWritable, Text, BytesWritable>{
        Text pathText = new Text();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            InputSplit inputSplit = context.getInputSplit();
            Path path = ((FileSplit)inputSplit).getPath();
            pathText.set(pathText.toString());
        }

        @Override
        protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
            System.out.println("**************");
            System.out.println(value.getBytes());
            System.out.println("**************");
            context.write(pathText,value);
        }
    }


    @Override
    public int run(String[] args) throws Exception {

        Configuration conf = new Configuration();
		/*System.setProperty("HADOOP_USER_NAME", "hadoop");*/
        String[] otherArgs = new GenericOptionsParser(conf, args)
                .getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: combinefiles <in> <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf,"combine small files to sequencefile");
        job.setJarByClass(CombineFile.class);

        job.setInputFormatClass(WholeInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);


        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        job.setMapperClass(CombineFileMapper.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir","/Users/chouc/Desktop/local_hadoop/hadoop-2.9.0");
        args=new String[]{"/Users/chouc/Desktop/local_hadoop/mycase/combineFile/input","/Users/chouc/Desktop/local_hadoop/mycase/combineFile/out"};
        int exitCode = ToolRunner.run(new CombineFile(),
                args);
        System.exit(exitCode);

    }


}
