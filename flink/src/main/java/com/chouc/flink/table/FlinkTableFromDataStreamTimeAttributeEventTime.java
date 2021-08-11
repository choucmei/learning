package com.chouc.flink.table;

import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.e;

// https://ci.apache.org/projects/flink/flink-docs-release-1.13/docs/dev/table/concepts/time_attributes/
public class FlinkTableFromDataStreamTimeAttributeEventTime {
    public static void main(String[] args) {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(streamExecutionEnvironment);
        DataStreamSource<StreamSourceUtils.CustomRecord> socketStream = StreamSourceUtils.getKafkaStream("user_behavior");
        //         streamTableEnvironment.fromDataStream(socketStream, $("offset"), $("value"), $("timestamp"), $("rt").rowtime()).execute().print();

        /**
         * CREATE TABLE user_actions (
         *   user_name STRING,
         *   data STRING,
         *   user_action_time TIMESTAMP(3),
         *   -- declare user_action_time as event time attribute and use 5 seconds delayed watermark strategy
         *   WATERMARK FOR user_action_time AS user_action_time - INTERVAL '5' SECOND
         * ) WITH (
         *   ...
         * );
         *
         * SELECT TUMBLE_START(user_action_time, INTERVAL '10' MINUTE), COUNT(DISTINCT user_name)
         * FROM user_actions
         * GROUP BY TUMBLE(user_action_time, INTERVAL '10' MINUTE);
         */
        Table table = streamTableEnvironment.fromDataStream(socketStream, $("offset"), $("value"), $("timestamp").rowtime());
        table.printSchema();
        table.execute().print();
    }
}
