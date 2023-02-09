package com.chouc.flink.test;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;

/**
 * @Author choucmei
 * @create 2022/5/27
 */
public class GerateToMysql {
    public static void main(String[] args) {
        // 进行Kerberos 认证
        System.setProperty("HADOOP_USER_NAME", "hive");
        System.setProperty("java.security.krb5.realm", "UAT.JZBD.COM");
        System.setProperty("java.security.krb5.kdc", "tmaster:88");

        Configuration conf = new Configuration();
        conf.set("hadoop.security.authentication", "Kerberos");
        UserGroupInformation.setConfiguration(conf);
        try {
            UserGroupInformation.loginUserFromKeytab("meixb001/user@UAT.JZBD.COM", "C:\\Users\\96204\\Desktop\\keytabs\\meixb001.user.keytab");
        } catch (IOException e) {
            e.printStackTrace();
        }

        org.apache.flink.configuration.Configuration configuration = new org.apache.flink.configuration.Configuration();
        configuration.set(RestOptions.BIND_PORT, "8082");
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        executionEnvironment.enableCheckpointing(1000 * 60, CheckpointingMode.EXACTLY_ONCE);
        executionEnvironment.getConfig().setAutoWatermarkInterval(1000 * 10);
        executionEnvironment.getCheckpointConfig().setCheckpointStorage(new Path("hdfs://tmaster:9000/tmp/flink_checkpoint/generate_data"));
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(executionEnvironment);
        DataStreamSource<Tuple2<String, Long>> tuple2DataStreamSource = executionEnvironment.addSource(new SourceFunction<Tuple2<String, Long>>() {
            @Override
            public void run(SourceContext<Tuple2<String, Long>> ctx) throws Exception {
                long i = 0L;
                while (true) {
                    ctx.collect(Tuple2.of(String.valueOf("id:" + i), i));
                    i += 1l;
                    Thread.sleep(1000 * 4);
                }
            }

            @Override
            public void cancel() {

            }
        });

        class T implements CheckpointedFunction, MapFunction<Tuple2<String, Long>, Tuple2<String, Long>> {


            @Override
            public void snapshotState(FunctionSnapshotContext context) throws Exception {
                System.out.println("************** snapshotState ***************");
            }

            @Override
            public void initializeState(FunctionInitializationContext context) throws Exception {

            }

            @Override
            public Tuple2<String, Long> map(Tuple2<String, Long> value) throws Exception {
                return value;
            }
        }


        Table table = streamTableEnvironment.fromDataStream(tuple2DataStreamSource.map(new T()).setParallelism(2));
        streamTableEnvironment.createTemporaryView("tmp", table);


        String sql = "CREATE TABLE MyUserTable (\n" +
                "  id STRING,\n" +
                "  num bigint\n" +
                ") WITH (\n" +
                "   'connector' = 'jdbc',\n" +
                "   'username' = 'root',\n" +
                "   'password' = '123456',\n" +
                "   'url' = 'jdbc:mysql://localhost:3306/test',\n" +
                "   'table-name' = 'orders'\n" +
                ");";

        streamTableEnvironment.executeSql(sql);

        streamTableEnvironment.executeSql("insert into MyUserTable select * from tmp").print();

    }
}
