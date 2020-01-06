package chouc.hadoop.mr.mapsidejoin;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


public class MapSideJoin {

    static Map<String,String> mapTem = new HashMap<String, String>();

    static class MapSideJoinMapper extends Mapper<LongWritable, Text, Text, NullWritable>{
        /**
         * 通过阅读父类Mapper的源码，发现 setup方法是在maptask处理数据之前调用一次 可以用来做一些初始化工作
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("info")));
            String line;
            while (!StringUtils.isEmpty(line = br.readLine())){
                String[] fields = line.split(",");
                mapTem.put(fields[0],fields[1]+"-"+fields[2]);
            }
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split(",");
            context.write(new Text(fields[0]+"-"+fields[1]+"-"+fields[2]+"-"+fields[3]+mapTem.get(fields[2])), NullWritable.get());
        }
    }

//  /Users/chouc/Desktop/local_hadoop/mycase/mapsidejoin/info

    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {
        System.setProperty("hadoop.home.dir","/Users/chouc/Desktop/local_hadoop/hadoop-2.9.0");
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        job.setMapperClass(MapSideJoinMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setNumReduceTasks(0);

        // 指定需要缓存一个文件到所有的maptask运行节点工作目录
		/* job.addArchiveToClassPath(archive); */// 缓存jar包到task运行节点的classpath中
		/* job.addFileToClassPath(file); */// 缓存普通文件到task运行节点的classpath中
		/* job.addCacheArchive(uri); */// 缓存压缩包文件到task运行节点的工作目录
		/* job.addCacheFile(uri) */// 缓存普通文件到task运行节点的工作目录

//        job.addCacheFile(new URI("/Users/chouc/Desktop/local_hadoop/mycase/mapsidejoin/info"));



        FileInputFormat.setInputPaths(job,new Path("/Users/chouc/Desktop/local_hadoop/mycase/mapsidejoin/input"));
        FileOutputFormat.setOutputPath(job,new Path("/Users/chouc/Desktop/local_hadoop/mycase/mapsidejoin/out"));
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }

}
