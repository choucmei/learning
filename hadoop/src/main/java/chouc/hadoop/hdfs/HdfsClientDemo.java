package chouc.hadoop.hdfs;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HdfsClientDemo {

    FileSystem fileSystem = null;


    @Before
    public void init() throws IOException, URISyntaxException, InterruptedException {
        try {
            Configuration conf = new Configuration();
            fileSystem = FileSystem.get(new URI("hdfs://10.211.55.18"),conf,"root");
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testUpload() throws IOException {
        fileSystem.copyFromLocalFile(new Path("/Users/chouc/Desktop/info"),new Path("/rjoin/input/info"));
    }

    @Test
    public void testDownload() throws IOException {
        fileSystem.copyToLocalFile(new Path("/dir/UtilsShell.py"),new Path("/Users/chouc/Desktop/UtilsShell2.py"));
    }

    @Test
    public void testDelete() throws IOException {
        fileSystem.delete(new Path("/rjoin/out"),true);
    }

    @Test
    public void testUploadByFlow() throws Exception {

            FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("/test.tar.gz"));
            FileInputStream fileInputStream = new FileInputStream("/Users/chouc/Desktop/test.tar.gz");
            IOUtils.copy(fileInputStream, fsDataOutputStream);

    }

    public static void main(String[] args) {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat smft=new SimpleDateFormat("YYYY/MM/dd/  HH:mm:ss E");
        String nowString=smft.format(calendar.getTime());
        System.out.println(nowString);
    }


}
