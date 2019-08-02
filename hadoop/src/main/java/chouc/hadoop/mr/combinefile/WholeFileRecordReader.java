package chouc.hadoop.mr.combinefile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;


public class WholeFileRecordReader extends RecordReader<NullWritable, BytesWritable> {
    private FileSplit fileSplit;
    private Configuration conf;
    private BytesWritable value = new BytesWritable();
    private boolean processed = false;

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        this.fileSplit = (FileSplit) inputSplit;
        this.conf = taskAttemptContext.getConfiguration();

    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!processed){
            byte[] targetContent = new byte[(int) fileSplit.getLength()];
            Path path = fileSplit.getPath();
            FileSystem fileSystem = path.getFileSystem(conf);
            FSDataInputStream fileInputStream = fileSystem.open(path);
//                fileInputStream.read(targetContent);
            try {
                IOUtils.readFully(fileInputStream, targetContent, 0, targetContent.length);
//                fileInputStream.read(targetContent);
                value.set(targetContent,0,targetContent.length);
            }finally {
                IOUtils.closeStream(fileInputStream);
            }
            processed=true;
            return true;
        }
        return false;
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }


    @Override
    public float getProgress() throws IOException, InterruptedException {
        return processed?1.0f:0.0f;
    }

    @Override
    public void close() throws IOException {

    }
}