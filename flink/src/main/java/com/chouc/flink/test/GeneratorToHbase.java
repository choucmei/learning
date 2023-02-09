package com.chouc.flink.test;

import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;

/**
 * @Author choucmei
 * @create 2022/5/25
 */
public class GeneratorToHbase {
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

        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.enableCheckpointing(1000 * 60, CheckpointingMode.EXACTLY_ONCE);
        executionEnvironment.getConfig().setAutoWatermarkInterval(1000 * 10);
        executionEnvironment.getCheckpointConfig().setCheckpointStorage(new Path("hdfs://tmaster:9000/tmp/flink_checkpoint/generate_data"));
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(executionEnvironment);

        String createHbaseMappingTable = "CREATE TABLE horders (\n" +
                " rowkey STRING,\n" +
                " cf1 ROW<user_id STRING,price decimal(5,2)>,\n" +
                " cf2 ROW<order_time TIMESTAMP_LTZ(0)>,\n" +
                " PRIMARY KEY (rowkey) NOT ENFORCED\n" +
                ") WITH (\n" +
                " 'connector' = 'hbase-2.2',\n" +
                " 'table-name' = 'namespace_test:orders',\n" +
                " 'sink.buffer-flush.max-size' = '100k',\n" +
                " 'sink.buffer-flush.max-rows' = '100',\n" +
                " 'sink.parallelism' = '2',\n" +
                " 'properties.hbase.security.authentication' = 'kerberos', \n" +
                " 'properties.hbase.regionserver.kerberos.principal' = 'hbase/_HOST@UAT.JZBD.COM', \n" +
                " 'properties.hbase.master.kerberos.principal'='hbase/_HOST@UAT.JZBD.COM', \n" +
                " 'zookeeper.quorum' = '192.168.3.204,192.168.3.205,192.168.3.209'\n" +
                ");\n";

        String createSourceTable = "CREATE TABLE orders (\n" +
                "    order_id STRING,\n" +
                "    user_id        STRING,\n" +
                "    price        DECIMAL(5,2),\n" +
                "    order_time   TIMESTAMP_LTZ(0),\n" +
                "    log_ts TIMESTAMP(3),\n" +
                "  WATERMARK FOR log_ts AS log_ts - INTERVAL '5' SECOND\n" +
                ") WITH (\n" +
                "  'connector' = 'datagen',\n" +
                "  'rows-per-second' = '5'\n" +
                ")";


        String insertToHive = "insert into `horders` select order_id,ROW(user_id,price),ROW(order_time) from orders";


        streamTableEnvironment.executeSql(createHbaseMappingTable).print();

        streamTableEnvironment.executeSql(createSourceTable).print();

        streamTableEnvironment.executeSql(insertToHive).print();

    }
}
