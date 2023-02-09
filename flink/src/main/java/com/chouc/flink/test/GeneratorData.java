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
public class GeneratorData {
    public static void main(String[] args) {
        // 进行Kerberos 认证
        System.setProperty("HADOOP_USER_NAME", "hive");
        System.setProperty("java.security.krb5.realm", "UAT.JZBD.COM");
        System.setProperty("java.security.krb5.kdc", "tmaster:88");

        Configuration conf = new Configuration();
        conf.set("hadoop.security.authentication", "Kerberos");
        UserGroupInformation.setConfiguration(conf);
        try {
            UserGroupInformation.loginUserFromKeytab("meixb001/user@UAT.JZBD.COM", "C:\\Users\\96204\\Desktop\\meixb001.user.keytab");
        } catch (IOException e) {
            e.printStackTrace();
        }

        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.enableCheckpointing(1000 * 60, CheckpointingMode.EXACTLY_ONCE);
        executionEnvironment.getConfig().setAutoWatermarkInterval(1000 * 10);
        executionEnvironment.getCheckpointConfig().setCheckpointStorage(new Path("hdfs://tmaster:9000/tmp/flink_checkpoint/generate_data"));
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(executionEnvironment);

        String createHiveTable = "CREATE TABLE `myhive`.`default`.`orders`(                              \n" +
                "   `order_id` STRING,                                   \n" +
                "   `user_id` STRING,                                 \n" +
                "   `price` decimal(5,2),                             \n" +
                "   `order_time` TIMESTAMP(8),\n" +
                "   `WATERMARK` FOR ts AS ts\n" +
                "   )                              \n" +
                " PARTITIONED BY (`dt` STRING, `hr` STRING)                                    \n" +
                " ROW FORMAT SERDE                                    \n" +
                "   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'       \n" +
                " STORED AS INPUTFORMAT                               \n" +
                "   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' \n" +
                " OUTPUTFORMAT                                        \n" +
                "   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'\n" +
                "TBLPROPERTIES (\n" +
                "  'partition.time-extractor.timestamp-pattern'='$dt $hr:00:00',\n" +
                "  'sink.partition-commit.trigger'='partition-time',\n" +
                "  'sink.partition-commit.watermark-time-zone'='Asia/Shanghai', -- Assume user configured time zone is 'Asia/Shanghai'\n" +
                "  'sink.partition-commit.policy.kind'='metastore,success-file'\n" +
                ");";

        String createSourceTable = "CREATE TABLE orders (\n" +
                "    order_id STRING,\n" +
                "    user_id        STRING,\n" +
                "    price        DECIMAL(5,2),\n" +
                "    order_time   TIMESTAMP_LTZ,\n" +
                "    log_ts TIMESTAMP(3),\n" +
                "  WATERMARK FOR log_ts AS log_ts - INTERVAL '5' SECOND\n" +
                ") WITH (\n" +
                "  'connector' = 'datagen',\n" +
                "  'rows-per-second' = '5'\n" +
                ")";
        String createHiveCatalog = "CREATE CATALOG myhive WITH (\n" +
                "  'type' = 'hive',\n" +
                "  'hive-conf-dir' = 'C:\\Users/96204/Desktop/conf/'\n" +
                ");";

        String insertToHive = "insert into `myhive`.`default`.`orders` select order_id,user_id,price,order_time,DATE_FORMAT(order_time, 'yyyy-MM-dd') as dt, DATE_FORMAT(order_time, 'HH') as hr from orders";


        streamTableEnvironment.executeSql(createHiveCatalog).print();

//        streamTableEnvironment.getConfig().setSqlDialect(SqlDialect.HIVE);
//
//        streamTableEnvironment.executeSql(createHiveTable).print();

//        streamTableEnvironment.getConfig().setSqlDialect(SqlDialect.DEFAULT);

        streamTableEnvironment.executeSql(createSourceTable).print();

        streamTableEnvironment.executeSql(insertToHive).print();

    }
}
