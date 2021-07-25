package com.chouc.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class FlinkTableApi {
    public static void main(String[] args) {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(executionEnvironment);
        String sourceSql = "CREATE TABLE user_behavior (\n" +
                "  user_id STRING,\n" +
                "  category_id STRING,\n" +
                "  behavior STRING\n" +
                ") WITH (\n" +
                "'connector' = 'filesystem', \n" +
                " 'path' = 'C:\\Users\\chouc\\Desktop\\csv_file.csv'," +
                " 'format' = 'csv',\n" +
                " 'csv.ignore-parse-errors' = 'true',\n" +
                " 'csv.allow-comments' = 'true'\n" +
                ")";

        String sinkSql = "CREATE TABLE user_behavior_sink (\n" +
                "  user_id STRING,\n" +
                "  category_id STRING,\n" +
                "  behavior STRING\n" +
                ") WITH (\n" +
                "'connector' = 'filesystem', \n" +
                " 'path' = 'C:\\Users\\chouc\\Desktop\\csv_file_sink1'," +
                " 'format' = 'csv',\n" +
                " 'csv.ignore-parse-errors' = 'true',\n" +
                " 'csv.allow-comments' = 'true'\n" +
                ")";
        String sinkSql2 = "CREATE TABLE user_behavior_sink2 (\n" +
                "  user_id STRING,\n" +
                "  category_id STRING,\n" +
                "  behavior STRING\n" +
                ") WITH (\n" +
                "'connector' = 'filesystem', \n" +
                " 'path' = 'C:\\Users\\chouc\\Desktop\\csv_file_sink2'," +
                " 'format' = 'csv',\n" +
                " 'csv.ignore-parse-errors' = 'true',\n" +
                " 'csv.allow-comments' = 'true'\n" +
                ")";
        tableEnv.executeSql(sourceSql);
        tableEnv.executeSql(sinkSql);
        tableEnv.executeSql(sinkSql2);
        TableResult tableResult1 = tableEnv.from("user_behavior").executeInsert("user_behavior_sink");
        tableResult1.print();
        String insertSql ="insert into user_behavior_sink2 select * from user_behavior";
        TableResult tableResult = tableEnv.executeSql(insertSql);
        tableResult.print();

    }
}