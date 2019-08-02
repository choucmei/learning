package chouc.hadoop.mr.logenhance;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class LogEnhanceOutputFormat extends FileOutputFormat<Text, NullWritable> {

    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(taskAttemptContext.getConfiguration());
        Path enPath = new Path("/Users/chouc/Desktop/local_hadoop/case/logenhance/output/enhance/en.data");
        Path crwPath = new Path("/Users/chouc/Desktop/local_hadoop/case/logenhance/output/crwPath/crw.data");
        FSDataOutputStream enhancedOs = fileSystem.create(enPath);
        FSDataOutputStream crawldOs = fileSystem.create(crwPath);
        return new EnhanceRecordWriter(enhancedOs,crawldOs);
    }


    /**
     * 构造一个自己的recordwriter
     */
    static class EnhanceRecordWriter extends RecordWriter<Text, NullWritable> {
        FSDataOutputStream enhancedOs = null;
        FSDataOutputStream tocrawlOs = null;

        public EnhanceRecordWriter(FSDataOutputStream enhancedOs, FSDataOutputStream tocrawlOs) {
            super();
            this.enhancedOs = enhancedOs;
            this.tocrawlOs = tocrawlOs;
        }

        @Override
        public void write(Text key, NullWritable value) throws IOException, InterruptedException {
            String result = key.toString();
            // 如果要写出的数据是待爬的url，则写入待爬清单文件 /logenhance/tocrawl/url.dat
            if (result.contains("tocrawl")) {
                tocrawlOs.write(result.getBytes());
            } else {
                // 如果要写出的数据是增强日志，则写入增强日志文件 /logenhance/enhancedlog/log.dat
                enhancedOs.write(result.getBytes());
            }

        }

        @Override
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            if (tocrawlOs != null) {
                tocrawlOs.close();
            }
            if (enhancedOs != null) {
                enhancedOs.close();
            }

        }

    }
}
