package com.chouc.flink.table;

import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.ExplainDetail;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.bridge.java.internal.StreamTableEnvironmentImpl;

import static org.apache.flink.table.api.Expressions.$;

public class FlinkTableSinkElasticSearch {
    public static void main(String[] args) {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnvironment =  StreamTableEnvironment.create(streamExecutionEnvironment);
        DataStreamSource<StreamSourceUtils.CustomRecord> kafkaStream = StreamSourceUtils.getKafkaStream();

        /**
         *  如果不设置主键就是 insert ，设置主键就是 upsert
         */
        String sourceSql = "CREATE TABLE es_sink_table (" +
                "  `topic` STRING," +
                "  `partition` BIGINT," +
                "  `cnt` BIGINT," +
                "  PRIMARY KEY (topic,`partition`) NOT ENFORCED" +
                ") WITH (" +
                "  'connector' = 'elasticsearch-7'," +
                "  'hosts' = 'http://s1:9200'," +
                "  'format' = 'json',"+
                "  'index' = 'users'" +
                ")";
        streamTableEnvironment.executeSql(sourceSql);
        System.out.println();
        Table table = streamTableEnvironment.fromDataStream(kafkaStream);
        Table tableAggr = table.groupBy($("topic"),$("partition")).select($("topic"),$("partition"), $("partition").count().as("cnt"));
        tableAggr.executeInsert("es_sink_table");

    }
}
