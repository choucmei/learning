package com.chouc.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * https://ci.apache.org/projects/flink/flink-docs-release-1.13/zh/docs/connectors/table/kafka/
 */
public class FlinkTableKafkaSource {
    public static void main(String[] args) {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(executionEnvironment);
        String sourceSql = "CREATE TABLE kafka_source_table (" +
                            "  `user_id` STRING," +
                            "  `item_id` STRING," +
                            "  `behavior` STRING," +
                            "  `offset` BIGINT METADATA FROM 'offset'," +
                            "  `ts` TIMESTAMP(3) METADATA FROM 'timestamp'" +
                            ") WITH (" +
                            "  'connector' = 'kafka'," +
                            "  'topic' = 'user_behavior'," +
                            "  'properties.bootstrap.servers' = 's1:9092'," +
                            "  'properties.group.id' = 'group1'," +
                            "  'scan.startup.mode' = 'latest-offset'," +
                            "  'format' = 'csv'" +
                            ")";
        tableEnvironment.executeSql(sourceSql);
        tableEnvironment.from("kafka_source_table").execute().print();
    }
}
