package chouc.hadoop.hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0
 * @Title: HBaseMapperReducer
 * @Description:
 */
public class HBaseMapperReducer {


    /**
     * 创建hbase配置
     */
    static Configuration config = null;
    static Admin admin = null;
    static {
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "s1,s2,s3");
        config.set("hbase.zookeeper.property.clientPort", "2181");
    }
    /**
     * 表信息
     */
    public static final String sourceTableName = "source";//表名1
    public static final String families = "content";//列族
    public static final String column = "info";//列
    public static final String sinkTableName = "sink";//表名2

    @Test
    public void generateHbaseData() throws Exception {
        Connection connection = ConnectionFactory.createConnection(config);
        admin = connection.getAdmin();
        if (admin.tableExists(TableName.valueOf(sourceTableName))){
            admin.disableTable(TableName.valueOf(sourceTableName));
            admin.deleteTable(TableName.valueOf(sourceTableName));
        }else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(sourceTableName));
            HColumnDescriptor family = new HColumnDescriptor(families); // 列族
            hTableDescriptor.addFamily(family);
            admin.createTable(hTableDescriptor);
        }

        Table table = connection.getTable(TableName.valueOf(sourceTableName));
        List<Put> lp = new ArrayList<Put>();
        Put p1 = new Put(Bytes.toBytes("1"));
        p1.addColumn(families.getBytes(), column.getBytes(),	("The Apache Hadoop software library is a framework").getBytes());
        lp.add(p1);
        Put p2 = new Put(Bytes.toBytes("2"));
        p2.addColumn(families.getBytes(),column.getBytes(),("The common utilities that support the other Hadoop modules").getBytes());
        lp.add(p2);
        Put p3 = new Put(Bytes.toBytes("3"));
        p3.addColumn(families.getBytes(), column.getBytes(),("Hadoop by reading the documentation").getBytes());
        lp.add(p3);
        Put p4 = new Put(Bytes.toBytes("4"));
        p4.addColumn(families.getBytes(), column.getBytes(),("Hadoop from the release page").getBytes());
        lp.add(p4);
        Put p5 = new Put(Bytes.toBytes("5"));
        p5.addColumn(families.getBytes(), column.getBytes(),("Hadoop on the mailing list").getBytes());
        lp.add(p5);
        table.put(lp);
        if (admin.tableExists(TableName.valueOf(sinkTableName))){
            admin.disableTable(TableName.valueOf(sinkTableName));
            admin.deleteTable(TableName.valueOf(sinkTableName));
        }else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(sinkTableName));
            HColumnDescriptor family = new HColumnDescriptor(families); // 列族
            hTableDescriptor.addFamily(family);
            admin.createTable(hTableDescriptor);
        }

        admin.close();
        table.close();
        connection.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
//            config.set("df.default.name", "hdfs://master:9000/");//设置hdfs的默认路径
//            config.set("hadoop.job.ugi", "hadoop,hadoop");//用户名，组
//            config.set("mapred.job.tracker", "master:9001");//设置jobtracker在哪
            //初始化表
            //创建job

            Job job = Job.getInstance(config, "HBaseMr");//job
            job.setJarByClass(HBaseMapperReducer.class);//主类

            //创建scan
            Scan scan = new Scan();
            //可以指定查询某一列
            scan.addColumn(Bytes.toBytes(families), Bytes.toBytes(column));
            //创建查询hbase的mapper，设置表名、scan、mapper类、mapper的输出key、mapper的输出value
            TableMapReduceUtil.initTableMapperJob(sourceTableName, scan, MyMapper.class, Text.class, IntWritable.class, job);
            //创建写入hbase的reducer，指定表名、reducer类、job
            TableMapReduceUtil.initTableReducerJob(sinkTableName, MyReducer.class, job);
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        }





    public static class MyMapper extends TableMapper<Text, IntWritable>  {
        private static IntWritable one = new IntWritable(1);
        private static Text word = new Text();
        public void map(ImmutableBytesWritable row, Result value, Mapper.Context context) throws IOException, InterruptedException {
            // this example is just copying the data from the source table...
            String words = Bytes.toString(value.getValue(Bytes.toBytes(families), Bytes.toBytes(column)));// 表里面只有一个列族，所以我就直接获取每一行的值
            //按空格分割
            String itr[] = words.toString().split(" ");
            //循环输出word和1
            for (int i = 0; i < itr.length; i++) {
                word.set(itr[i]);
                context.write(word, one);
            }
        }
    }

    public static class MyReducer extends TableReducer<Text, IntWritable,ImmutableBytesWritable>{
        int sumTemp = 0;
        ImmutableBytesWritable rowKeyTemp = new ImmutableBytesWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            for (IntWritable value:values){
                sumTemp+=value.get();
            }
            rowKeyTemp.set(key.getBytes());
            Put data = new Put(key.getBytes());
            data.addColumn(families.getBytes(),column.getBytes(),Bytes.toBytes(sumTemp));
            context.write(rowKeyTemp,data);
        }
    }

}
