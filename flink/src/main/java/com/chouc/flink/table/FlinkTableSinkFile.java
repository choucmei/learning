package com.chouc.flink.table;

import com.chouc.flink.utils.StreamSourceUtils;
import com.chouc.flink.utils.StreamSourceUtils.*;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

public class FlinkTableSinkFile {
    public static void main(String[] args) {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(streamExecutionEnvironment);
        DataStreamSource<CustomRecord> kafkaStream = StreamSourceUtils.getKafkaStream();
        String sourceSql = "CREATE TABLE user_behavior (\n" +
                "  `partition` BIGINT,\n" +
                "  `count` BIGINT \n" +
                ") WITH (\n" +
                "'connector' = 'filesystem', \n" +
                " 'path' = 'C:\\Users\\chouc\\Desktop\\csv_file'," +
                " 'format' = 'csv',\n" +
                " 'csv.ignore-parse-errors' = 'true',\n" +
                " 'csv.allow-comments' = 'true'\n" +
                ")";
        streamTableEnvironment.executeSql(sourceSql);
        Table table = streamTableEnvironment.fromDataStream(kafkaStream);
        Table tableAggr = table.groupBy($("partition")).select($("partition"), $("partition").count().as("count"));
        tableAggr.executeInsert("user_behavior");
        // doesn't support consuming update changes which is produced by node GroupAggregate
    }
}
